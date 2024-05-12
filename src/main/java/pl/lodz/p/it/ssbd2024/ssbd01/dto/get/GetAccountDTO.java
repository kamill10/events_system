package pl.lodz.p.it.ssbd2024.ssbd01.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.LanguageEnum;

import java.util.UUID;

public record GetAccountDTO(
        @NotNull
        UUID id,
        @Size(min = 3, max = 32)
        String username,
        @Email
        String email,
        @NotNull
        Integer gender,
        @Size(min = 2, max = 32)
        String firstName,
        @Size(min = 2, max = 64)
        String lastName,
        LanguageEnum  language
){}
