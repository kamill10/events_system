package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.entity;

import jakarta.persistence.*;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;
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
    private Boolean isConfirmed;
    private LocalDateTime reservationTime;

}
