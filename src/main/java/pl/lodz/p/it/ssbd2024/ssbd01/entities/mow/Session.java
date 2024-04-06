package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
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

    @Size(max =1024)
    private String description;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Column(nullable = false, length = 4)
    @Range(min = 0, max = 1024)
    @NotNull
    private Integer maxSeats;
}
