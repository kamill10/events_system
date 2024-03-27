package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.entity;

import jakarta.persistence.*;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.ControlledEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Ticket extends ControlledEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime reservationTime;
    @OneToOne
    private Account account;
    @OneToOne
    private Session session;
    private Boolean isConfirmed;

}
