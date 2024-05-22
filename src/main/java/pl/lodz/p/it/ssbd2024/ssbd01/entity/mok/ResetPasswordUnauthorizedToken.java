package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractCredentialChange;

import java.time.LocalDateTime;

@Table(name = "reset_password_unauthorized_token")
@Entity
@NoArgsConstructor
public class ResetPasswordUnauthorizedToken extends AbstractCredentialChange {
    public ResetPasswordUnauthorizedToken(String token, Account account, LocalDateTime expirationDate) {
        super(token, account, expirationDate);
    }
}
