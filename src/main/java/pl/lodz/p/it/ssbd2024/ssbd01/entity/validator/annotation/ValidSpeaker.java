package pl.lodz.p.it.ssbd2024.ssbd01.entity.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.validator.validator.ValidSpeakerValidator;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidSpeakerValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSpeaker {
    String message() default ExceptionMessages.INCORRECT_SPEAKER;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
