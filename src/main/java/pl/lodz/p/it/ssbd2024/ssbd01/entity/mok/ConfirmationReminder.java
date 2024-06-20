package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_reminder")
@NoArgsConstructor
@Getter
public class ConfirmationReminder extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    @NotNull(message = ExceptionMessages.INCORRECT_ACCOUNT)
    private Account account;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reminderDate;

    public ConfirmationReminder(Account account, LocalDateTime reminderDate) {
        this.account = account;
        this.reminderDate = reminderDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfirmationReminder that)) {
            return false;
        }

        return getAccount().equals(that.getAccount());
    }

    @Override
    public int hashCode() {
        return getAccount().hashCode();
    }
}
