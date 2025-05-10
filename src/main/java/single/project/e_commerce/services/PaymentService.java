package single.project.e_commerce.services;


import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import single.project.e_commerce.configuration.VNPayConfig;
import single.project.e_commerce.dto.request.AfterPaymentRequestDTO;
import single.project.e_commerce.dto.request.PaymentRequestDTO;
import single.project.e_commerce.dto.response.PaymentSuccessResponseDTO;
import single.project.e_commerce.dto.response.PaymentUrlResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.models.*;
import single.project.e_commerce.repositories.*;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.commons.VNPayUtils;
import single.project.e_commerce.utils.enums.ErrorCode;
import single.project.e_commerce.utils.enums.PaymentMethod;
import single.project.e_commerce.utils.enums.ShippingStatus;
import single.project.e_commerce.utils.enums.TokenType;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final VNPayConfig vnPayConfig;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ShipmentRepository shipmentRepository;
    private final ProductRepository productRepository;

    public PaymentUrlResponseDTO checkout(PaymentRequestDTO dto, HttpServletRequest request, HttpServletResponse response) {
        Set<Long> orderIds = dto.getOrderIds();
        List<Order> orders = orderRepository.findAllOrdersWithIdsNoCollectionFetching(new ArrayList<>(orderIds));
        double total_amount = 0;
        for (Order order : orders) {
            total_amount += order.getTotalPrice();
        }
        long afterRoundTotalAmount = Math.round(total_amount);
        String bankCode = dto.getBankCode();

        // create cookies store token and orderIds
        String orderIdsString = orderIds.stream().map(String::valueOf)
                .collect(Collectors.joining(","));
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        String token = authorization.substring("Bearer ".length());
        Cookie orderIdsCookie = createCookie("orderIds", orderIdsString, "/api/payment", 3000);
        Cookie tokenCookie = createCookie("token", token, "/api/payment", 3000);

        response.addCookie(orderIdsCookie);
        response.addCookie(tokenCookie);

        // return dto response
        return PaymentUrlResponseDTO.builder()
                .url(createPaymentUrl(String.valueOf(afterRoundTotalAmount), bankCode, request))
                .build();
    }


    private String createPaymentUrl(String amountString, String bankCode, HttpServletRequest request) {
        long amount = Integer.parseInt(amountString) * 100L;

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);

        // build hash key from secret key and hashData
        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }

    public PaymentSuccessResponseDTO success(String orderIdsString, String token, HttpServletResponse response) {
        // get orderIds and token from cookie
        List<Long> orderIds = Arrays.stream(orderIdsString.split(","))
                .map(Long::parseLong).toList();
        //validate token
        jwtService.validateToken(token, TokenType.ACCESS);
        // after get value of cookie, remove the cookie
        removeCookie("orderIds", "/api/payment", response);
        removeCookie("token", "/api/payment", response);
        return PaymentSuccessResponseDTO.builder()
                .orderIds(orderIds)
                .message("Payment successfully, your order will be updated and set to the shipment service")
                // FE use this orderIds to request create payment and shipping
                .build();
    }


    @Transactional
    public String createPaymentAndShipping(AfterPaymentRequestDTO dto) {
        List<Long> orderIds = dto.getOrderIds();
        String username = GlobalMethod.extractUserFromContext();
        log.info("find user");
        User user = userRepository.findUserWithNoCollectionByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        log.info("find orders");
        List<Order> orders = orderRepository.findAllOrdersByIdsWithCollection(orderIds);

        log.info("find orderDetails");
        List<Object[]> orderDetailsWithShop = orderDetailRepository.findAllByOrderIdsWithShop(orderIds);
        Map<Long, Long> odIdShopIdMap = orderDetailsWithShop.stream()
                .collect(Collectors.toMap(
                        ods -> ((OrderDetail) ods[0]).getId(),
                        ods -> (Long) ods[1]));
        Map<Long, Product> odIdProductMap = orderDetailsWithShop.stream()
                .collect(Collectors.toMap(
                        ods -> ((OrderDetail) ods[0]).getId(),
                        ods -> (Product) ods[2]));
        List<Payment> payments = new ArrayList<>();
        List<Shipment> shipments = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        for (Order order : orders) {
            payments.add(Payment.builder()
                    .price(order.getTotalPrice())
                    .paymentMethod(PaymentMethod.VNPAY)
                    .paidAt(new Date())
                    .order(order)
                    .user(user)
                    .build());
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                // update product quantity
                Product product = odIdProductMap.get(orderDetail.getId());
                if (orderDetail.getQuantity() <= 0 || product.getRemainingQuantity() < orderDetail.getQuantity()) {
                    throw new AppException(ErrorCode.QUANTITY_INVALID);
                }
                product.setSoldQuantity(product.getSoldQuantity() + orderDetail.getQuantity());
                product.setRemainingQuantity(product.getRemainingQuantity() - orderDetail.getQuantity());
                products.add(product);
                // create shipment
                shipments.add(Shipment.builder()
                        .trackingId(UUID.randomUUID().toString())
                        .receiverAddress(order.getReceiverAddress())
                        .receiverNumber(order.getReceiverNumber())
                        .shippingStatus(ShippingStatus.SHIPPING)
                        .orderDetail(orderDetail)
                        .shop(Shop.builder().id(odIdShopIdMap.get(orderDetail.getId())).build())
                        .user(user)
                        .build());
            }
        }
        paymentRepository.saveAll(payments);
        shipmentRepository.saveAll(shipments);
        productRepository.saveAll(products);
        return "Create payment and shipping successfully,please check information again to assure nothing wrong";
    }


    private Cookie createCookie(String key, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void removeCookie(String key, String path, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath(path);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
