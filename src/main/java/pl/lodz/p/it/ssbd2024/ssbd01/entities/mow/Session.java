package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank
    @Size(max = 32)
    private String name;

    @Column(nullable = false)
    @NotNull
    private Boolean isActive;

    @Size(max = 1024)
    private String description;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Column(nullable = false, length = 4)
    @NotNull
    @Min(0)
    @Max(1024)
    private Integer maxSeats;
}
