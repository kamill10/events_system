package pl.lodz.p.it.ssbd2024.ssbd01.entity.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ControlledEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "ticket")
@NoArgsConstructor
public class Ticket extends ControlledEntity {

    @OneToOne
    @JoinColumn(nullable = false, name = "account_id")
    @NotNull
    private Account account;

    @OneToOne
    @JoinColumn(nullable = false, name = "session_id")
    @NotNull
    private Session session;

    @Column(nullable = false)
    @NotNull
    private Boolean isConfirmed;

    @PastOrPresent
    @NotNull
    private LocalDateTime reservationTime;

    public Ticket(Account account, Session session) {
        this.account = account;
        this.session = session;
        this.isConfirmed = false;
        this.reservationTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket ticket)) {
            return false;
        }
        return Objects.equals(account, ticket.account) && Objects.equals(session, ticket.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, session);
    }
}
