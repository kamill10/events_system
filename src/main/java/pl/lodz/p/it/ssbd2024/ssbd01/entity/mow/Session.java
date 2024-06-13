package pl.lodz.p.it.ssbd2024.ssbd01.entity.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ControlledEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "session")
@NoArgsConstructor
public class Session extends ControlledEntity {

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false, name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(nullable = false, name = "speaker_id")
    @NotNull
    private Speaker speaker;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 64)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Ticket> tickets = new ArrayList<>();

    @Column(nullable = false)
    @NotNull
    private Boolean isActive;

    @Size(min = 3, max = 1024)
    private String description;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Column(nullable = false, length = 4)
    @NotNull
    @Min(1)
    @Max(1024)
    private Integer maxSeats;

    @PositiveOrZero
    private long counter;

    @Min(0)
    private int availableSeats;

    public Session(Room room, Speaker speaker, String name, Event event,
                   Boolean isActive, String description, LocalDateTime startTime,
                   LocalDateTime endTime, Integer maxSeats) {
        this.room = room;
        this.speaker = speaker;
        this.name = name;
        this.event = event;
        this.isActive = isActive;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxSeats = maxSeats;
        this.availableSeats = maxSeats;
    }

    public Session(String name, String description, LocalDateTime startTime,
                   LocalDateTime endTime, Integer maxSeats) {
        this.name = name;
        this.isActive = true;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxSeats = maxSeats;
        this.availableSeats = maxSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session session)) {
            return false;
        }
        return Objects.equals(name, session.name) && Objects.equals(event, session.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, event);
    }
}