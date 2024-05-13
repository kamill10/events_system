package pl.lodz.p.it.ssbd2024.ssbd01.util;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "credential_change_token")
public abstract class AbstractCredentialChange extends AbstractEntity {
    @Column(nullable = false, unique = true)
    @NotNull
    private  String token;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false,updatable = false)
    @NotNull
    private Account account;

    @Column(nullable = false)
    @NotNull
    @Future
    private LocalDateTime expirationDate;

    public AbstractCredentialChange(String token, Account account, LocalDateTime expirationDate) {
        this.token = token;
        this.account = account;
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractCredentialChange that = (AbstractCredentialChange) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
