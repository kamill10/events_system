package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.ControlledEntity;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Session extends ControlledEntity {

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

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(nullable = false)
    private Event event;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session session)) return false;
        return Objects.equals(name, session.name) && Objects.equals(event, session.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, event);
    }
}
