package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "change_my_password")
public class ChangeMyPassword extends AbstractEntity {
    @Column(nullable = false, unique = true)
    @NotNull
    private String token;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    @NotNull
    Account account;

    @Column(nullable = false)
    @NotNull
    @Future
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    @Size(min = 8, max = 72)
    private String password;

    public ChangeMyPassword(String token, Account account, LocalDateTime expirationDate, String password) {
        this.token = token;
        this.account = account;
        this.expirationDate = expirationDate;
        this.password = password;
    }
}
