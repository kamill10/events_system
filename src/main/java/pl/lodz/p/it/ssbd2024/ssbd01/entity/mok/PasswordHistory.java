package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PasswordHistory extends AbstractEntity {

   /* @Column(name = "userId", columnDefinition = "UUID", updatable = false)
    @NotNull
    @Setter(AccessLevel.NONE)
    UUID userId;*/

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;
    @ToString.Exclude
    @Column(nullable = false, length = 72)
    @Size(min = 8, max = 72)
    @NotNull
    String password;

    public PasswordHistory(Account account, String password) {
        this.account = account;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PasswordHistory passwordHistory1 = (PasswordHistory) o;
        return Objects.equals(password, passwordHistory1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
