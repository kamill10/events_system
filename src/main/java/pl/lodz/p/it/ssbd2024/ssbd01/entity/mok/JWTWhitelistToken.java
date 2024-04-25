package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.util.Date;

@Entity
@Table(name = "jwt_whitelist_token")
@NoArgsConstructor
@Getter
public class JWTWhitelistToken extends AbstractEntity {
    private String token;
    private Date expirationDate;

    public JWTWhitelistToken(String token, Date expirationDate) {
        this.token = token;
        this.expirationDate = expirationDate;
    }
}
