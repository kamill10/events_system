package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.util.Objects;

@Entity
@Table(name = "account_unlock")
@NoArgsConstructor
@Getter
public class AccountUnlock extends AbstractEntity {

    @Column(nullable = false, unique = true)
    @NotNull
    private String token;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull
    private Account account;

    public AccountUnlock(String token, Account account) {
        this.token = token;
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountUnlock that)) {
            return false;
        }
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }

}
