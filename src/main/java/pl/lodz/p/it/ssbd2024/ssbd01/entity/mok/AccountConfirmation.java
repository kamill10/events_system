package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class AccountConfirmation extends AbstractEntity {

    @Column(nullable = false, unique = true)
    @NotNull
    private String token;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull
    private Account account;

    @Column(nullable = false)
    @NotNull
    @Future
    private LocalDateTime expirationDate;

    public AccountConfirmation(String token, Account account, LocalDateTime expirationDate) {
        this.token = token;
        this.account = account;
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountConfirmation that)) {
            return false;
        }
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }

}
