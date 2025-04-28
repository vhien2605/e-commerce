package single.project.e_commerce.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import single.project.e_commerce.exceptions.AppException;
import single.project.e_commerce.utils.annotations.PhoneNumber;
import single.project.e_commerce.utils.commons.AppConst;
import single.project.e_commerce.utils.enums.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    Pattern pattern;

    @Override
    public void initialize(PhoneNumber annotation) {
        try {
            pattern = Pattern.compile(AppConst.PHONE_NUMBER);
        } catch (PatternSyntaxException e) {
            throw new AppException(ErrorCode.REGEX_INVALID);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
