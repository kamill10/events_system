package pl.lodz.p.it.ssbd2024.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

public record MailToVerifyDTO(
        @NotNull
        Account account,
        @NotNull
        String token
) {
}
