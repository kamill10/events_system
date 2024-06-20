package pl.lodz.p.it.ssbd2024.ssbd01.entity.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Speaker;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.validator.annotation.ValidSpeaker;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.util.regex.Pattern;

public class ValidSpeakerValidator implements ConstraintValidator<ValidSpeaker, Speaker> {

    @Override
    public void initialize(ValidSpeaker constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Speaker speaker, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = true;

        if (speaker == null) {
            isValid = false;
            return isValid;
        }
        Pattern firstNamePattern = Pattern.compile("^[A-Z][a-zA-Z]{1,31}$");
        Pattern lastNamePattern = Pattern.compile("^[A-Z][a-zA-Z]{1,63}$");

        if (speaker.getFirstName() == null || !firstNamePattern.matcher(speaker.getFirstName()).matches()) {
            isValid = false;
            constraintValidatorContext.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_FIRST_NAME)
                    .addPropertyNode("firstName")
                    .addConstraintViolation();
        }
        if (speaker.getLastName() == null || !lastNamePattern.matcher(speaker.getLastName()).matches()) {
            isValid = false;
            constraintValidatorContext.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_LAST_NAME)
                    .addPropertyNode("lastName")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
