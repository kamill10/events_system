package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.entity;

import jakarta.persistence.*;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Session extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne
    private Room room;
    @ManyToOne
    private Speaker speaker;
    private String name;
    private Boolean isActive;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxSeats;
}
