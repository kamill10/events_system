package pl.lodz.p.it.ssbd2024.ssbd01.entity.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.validator.annotation.ValidLocation;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;


public class ValidLocationValidator implements ConstraintValidator<ValidLocation, Location> {

    @Override
    public void initialize(ValidLocation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Location location, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (location == null) {
            return true;
        }

        if (location.getName() == null || location.getName().isBlank() || location.getName().length() < 3 || location.getName().length() > 128) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_NAME).addConstraintViolation();
        }

        if (location.getStreet() == null || location.getStreet().isBlank()) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_STREET).addConstraintViolation();
        }

        if (location.getBuildingNumber() == null || location.getBuildingNumber().isBlank()) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_BUILDING_NUMBER).addConstraintViolation();
        }

        if (location.getPostalCode() == null || !location.getPostalCode().matches("\\d{2}-\\d{3}")) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_POSTAL_CODE).addConstraintViolation();
        }

        if (location.getCity() == null || location.getCity().isBlank()) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_CITY).addConstraintViolation();
        }

        if (location.getCountry() == null || location.getCountry().isBlank()) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_COUNTRY).addConstraintViolation();
        }

        if (location.getIsActive() == null) {
            isValid = false;
            context.buildConstraintViolationWithTemplate(ExceptionMessages.INCORRECT_ACTIVE_STATUS).addConstraintViolation();
        }

        return isValid;
    }
}
