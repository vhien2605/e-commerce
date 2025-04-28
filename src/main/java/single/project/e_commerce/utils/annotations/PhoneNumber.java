package single.project.e_commerce.utils.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import single.project.e_commerce.utils.validators.EnumPatternValidator;
import single.project.e_commerce.utils.validators.PhoneNumberValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "{name} must match {regexp}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
