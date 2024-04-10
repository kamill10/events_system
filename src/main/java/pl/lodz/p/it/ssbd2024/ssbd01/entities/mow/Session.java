package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.ControlledEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();

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

    public Session(Room room, Speaker speaker, String name, List<Event> events,
                   Boolean isActive, String description, LocalDateTime startTime,
                   LocalDateTime endTime, Integer maxSeats) {
        this.room = room;
        this.speaker = speaker;
        this.name = name;
        this.events = events;
        this.isActive = isActive;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxSeats = maxSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session session)) return false;
        return Objects.equals(name, session.name) && Objects.equals(events, session.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, events);
    }
}
