package single.project.e_commerce.services;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import single.project.e_commerce.configuration.VNPayConfig;
import single.project.e_commerce.dto.request.AfterPaymentRequestDTO;
import single.project.e_commerce.dto.request.PaymentRequestDTO;
import single.project.e_commerce.dto.response.PaymentStatusResponseDTO;
import single.project.e_commerce.dto.response.CheckoutResponseDTO;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.models.*;
import single.project.e_commerce.repositories.*;
import single.project.e_commerce.utils.commons.AppConst;
import single.project.e_commerce.utils.commons.GlobalMethod;
import single.project.e_commerce.utils.commons.VNPayUtils;
import single.project.e_commerce.utils.enums.ErrorCode;
import single.project.e_commerce.utils.enums.OrderStatus;
import single.project.e_commerce.utils.enums.PaymentMethod;
import single.project.e_commerce.utils.enums.ShippingStatus;


import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final VNPayConfig vnPayConfig;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ShipmentRepository shipmentRepository;
    private final ProductRepository productRepository;

    public CheckoutResponseDTO checkout(PaymentRequestDTO dto, HttpServletRequest request) {
        Set<Long> orderIds = dto.getOrderIds();
        // if customer choose to pay in cast, then return direct
        if (dto.getPaymentMethod().equals(PaymentMethod.PAY_WHEN_RECEIVED)) {
            return CheckoutResponseDTO.builder()
                    .url(null)
                    .orderIds(orderIds.stream().toList())
                    .paymentMethod(PaymentMethod.PAY_WHEN_RECEIVED)
                    .message("You chose pay in cast!. Thank you for using our services")
                    .build();
        }

        // calculate the total_price
        List<Order> orders = orderRepository.findAllOrdersWithIdsNoCollectionFetching(new ArrayList<>(orderIds));
        double total_amount = 0;
        for (Order order : orders) {
            total_amount += order.getTotalPrice();
        }
        long afterRoundTotalAmount = Math.round(total_amount);
        String bankCode = dto.getBankCode();

        // return dto response
        return CheckoutResponseDTO.builder()
                .url(createPaymentUrl(String.valueOf(afterRoundTotalAmount), bankCode, request))
                .paymentMethod(PaymentMethod.VNPAY)
                .orderIds(orderIds.stream().toList())
                .message("You chose pay by VNPAY!. Thank you for using our services")
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

    public PaymentStatusResponseDTO paymentStatus(String status) {
        Map<String, String> map = AppConst.VNPAY_ERRORCODE_MAP;
        return PaymentStatusResponseDTO.builder()
                .status(status)
                .message(map.get(status))
                .build();
    }


    @Transactional
    public String createPaymentAndShipping(AfterPaymentRequestDTO dto) {
        List<Long> orderIds = dto.getOrderIds();
        // update order status to PAID
        log.info("update order status to PAID");
        orderRepository.updateStatusOrderByOrderIds(orderIds, OrderStatus.PAID);

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
                    .paymentMethod(dto.getPaymentMethod())
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
}
