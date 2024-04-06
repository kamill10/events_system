package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.time.LocalDateTime;

@Data
@Entity
public class Ticket extends AbstractEntity {

    @OneToOne
    @JoinColumn(nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(nullable = false)
    private Session session;

    @Column(nullable = false)
    @NotNull
    private Boolean isConfirmed;

    @PastOrPresent
    private LocalDateTime reservationTime;

}
