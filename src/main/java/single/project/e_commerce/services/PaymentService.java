package single.project.e_commerce.services;


import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import single.project.e_commerce.configuration.VNPayConfig;
import single.project.e_commerce.dto.request.PaymentRequestDTO;
import single.project.e_commerce.dto.response.PaymentUrlResponseDTO;
import single.project.e_commerce.models.Order;
import single.project.e_commerce.repositories.OrderRepository;
import single.project.e_commerce.repositories.PaymentRepository;
import single.project.e_commerce.utils.commons.VNPayUtils;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final VNPayConfig vnPayConfig;
    private final OrderRepository orderRepository;


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

    public String success(String orderIdsString, HttpServletResponse response) {

        return "me may beo";
    }

    public String failed(HttpServletResponse response) {

        return "me may khong beo";
    }
}
