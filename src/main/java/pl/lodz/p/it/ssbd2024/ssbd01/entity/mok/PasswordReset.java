package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "passreset")
public class PasswordReset extends AbstractEntity {

    @Column(nullable = false, unique = true)
    @NotNull
    private String token;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false,updatable = false)
    @NotNull
    Account account;

    @Column(nullable = false)
    @NotNull
    private Boolean used;

    @Column(nullable = false)
    @NotNull
    @Future
    private LocalDateTime expirationDate;

    public PasswordReset(String token, Account account, LocalDateTime expirationDate) {
        this.token = token;
        this.account = account;
        this.used = false;
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PasswordReset that)) {
            return false;
        }
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }
}
