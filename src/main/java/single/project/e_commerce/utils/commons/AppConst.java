package single.project.e_commerce.utils.commons;

import java.util.Map;

public class AppConst {
    public static String SORT_BY = "(\\w+?)(:)(.*)";
    public static String SEARCH_SPEC_OPERATOR = "(')?(\\w+?)([<:>~!])([a-zA-Z0-9\\s]*)(\\p{Punct}?)(\\p{Punct}?)";
    public static String PHONE_NUMBER = "^[0-9\\-\\+]{9,15}$";
    public static Map<String, String> VNPAY_ERRORCODE_MAP = Map.ofEntries(
            Map.entry("00", "Giao dịch thành công"),
            Map.entry("07", "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường)."),
            Map.entry("09", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng."),
            Map.entry("10", "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần"),
            Map.entry("11", "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch."),
            Map.entry("12", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa."),
            Map.entry("13", "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch."),
            Map.entry("24", "Giao dịch không thành công do: Khách hàng hủy giao dịch"),
            Map.entry("51", "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch."),
            Map.entry("65", "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày."),
            Map.entry("75", "Ngân hàng thanh toán đang bảo trì."),
            Map.entry("79", "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch"),
            Map.entry("99", "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)")
    );
    
}
