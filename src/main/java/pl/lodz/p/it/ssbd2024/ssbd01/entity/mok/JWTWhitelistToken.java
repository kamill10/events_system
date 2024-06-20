package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "jwt_whitelist_token")
@NoArgsConstructor
@Getter
public class JWTWhitelistToken extends AbstractEntity {

    @Column(unique = true, nullable = false, updatable = false)
    private String token;

    @Future(message = ExceptionMessages.INCORRECT_EXPIRATION_DATE)
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public JWTWhitelistToken(String token, Date expirationDate, Account account) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JWTWhitelistToken that)) {
            return false;
        }

        return getToken().equals(that.getToken());
    }

    @Override
    public int hashCode() {
        return getToken().hashCode();
    }
}
