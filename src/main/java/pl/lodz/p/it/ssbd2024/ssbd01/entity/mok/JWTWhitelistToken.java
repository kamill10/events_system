package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "jwt_whitelist_token")
@NoArgsConstructor
@Getter
public class JWTWhitelistToken extends AbstractEntity {
    private String token;
    private Date expirationDate;


    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public JWTWhitelistToken(String token, Date expirationDate, Account account) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.account = account;
    }
}
