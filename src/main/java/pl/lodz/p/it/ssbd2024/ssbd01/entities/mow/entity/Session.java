package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.entity;

import jakarta.persistence.*;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.time.LocalDateTime;

@Data
@Entity
public class Session extends AbstractEntity {

    @OneToOne
    @JoinColumn(nullable = false)
    private Room room;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Speaker speaker;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Boolean isActive;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Column(nullable = false, length = 4)
    private Integer maxSeats;
}
