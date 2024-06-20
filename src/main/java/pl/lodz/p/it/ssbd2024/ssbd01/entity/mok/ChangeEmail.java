package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractCredentialChange;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "change_email")
public class ChangeEmail extends AbstractCredentialChange {

    @Column(nullable = false)
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    private String email;

    public ChangeEmail(Account account, LocalDateTime expirationDate, String email) {
        super(account, expirationDate);
        this.email = email;
    }
}
