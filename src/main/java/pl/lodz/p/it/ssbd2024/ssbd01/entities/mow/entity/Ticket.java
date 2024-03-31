package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.entity;

import jakarta.persistence.*;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Ticket extends AbstractEntity {

    @OneToOne
    @Column(nullable = false)
    private Account account;
    @OneToOne
    @Column(nullable = false)
    private Session session;
    @Column(nullable = false)
    private Boolean isConfirmed;
    private LocalDateTime reservationTime;

}
