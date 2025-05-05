package single.project.e_commerce.services;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import single.project.e_commerce.configuration.VNPayConfig;
import single.project.e_commerce.dto.response.PaymentUrlResponseDTO;
import single.project.e_commerce.repositories.PaymentRepository;
import single.project.e_commerce.utils.commons.VNPayUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final VNPayConfig vnPayConfig;

    public PaymentUrlResponseDTO createPaymentUrl(String amountString, String bankCode, HttpServletRequest request) {
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
        String url = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentUrlResponseDTO.builder()
                .url(url)
                .build();
    }

    public PaymentUrlResponseDTO checkout() {
        return null;
    }

    public String success() {
        return "";
    }
}
