package single.project.e_commerce.utils.commons;

public class AppConst {
    public static String SORT_BY = "(\\w+?)(:)(.*)";
    public static String SEARCH_SPEC_OPERATOR = "(')?(\\w+?)([<:>~!])([a-zA-Z0-9]*)(\\p{Punct}?)(\\p{Punct}?)";
    // dấu chấm để phân cách 2 khối Punct
    // ví dụ name:hien.,age>5.
}
