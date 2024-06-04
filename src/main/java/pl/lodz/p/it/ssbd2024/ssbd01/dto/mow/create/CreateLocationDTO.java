package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

public record CreateLocationDTO(

        @NotBlank(message = ExceptionMessages.INCORRECT_NAME)
        @Size(min = 3, max = 32, message = ExceptionMessages.INCORRECT_NAME)
        String name,

        @NotBlank(message = ExceptionMessages.INCORRECT_STREET)
        String street,

        @NotBlank(message = ExceptionMessages.INCORRECT_BUILDING_NUMBER)
        String buildingNumber,

        @NotBlank(message = ExceptionMessages.INCORRECT_POSTAL_CODE)
        @Pattern(regexp = "\\d{2}-\\d{3}", message = ExceptionMessages.INCORRECT_POSTAL_CODE)
        String postalCode,

        @NotBlank(message = ExceptionMessages.INCORRECT_CITY)
        String city,

        @NotBlank(message = ExceptionMessages.INCORRECT_COUNTRY)
        String country
) {
}
