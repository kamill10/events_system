package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;
import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_reminder")
@NoArgsConstructor
@Getter
public class ConfirmationReminder extends AbstractEntity {
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false,updatable = false)
    @NotNull
    private Account account;

    @Column(nullable = false,updatable = false)
    private LocalDateTime reminderDate;

    public ConfirmationReminder(Account account, LocalDateTime reminderDate) {
        this.account = account;
        this.reminderDate = reminderDate;
    }
}
