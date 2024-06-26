package pl.lodz.p.it.ssbd2024.ssbd01.entity.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.validator.validator.ValidLocationValidator;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidLocationValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocation {
    String message() default ExceptionMessages.INCORRECT_LOCATION;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
