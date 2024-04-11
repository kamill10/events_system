package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.ControlledEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Event extends ControlledEntity {

    @Column(nullable = false, unique = true, updatable = false)
    @NotBlank
    @Size(max = 128)
    @NotNull
    private String name;

    @Size(max = 1024)
    @NotNull
    private String description;

    @Column(nullable = false)
    @NotNull
    private Boolean isNotCanceled = true;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    @PositiveOrZero
    private long counter;

    public Event(String name, String description, List<Session> sessions) {
        this.name = name;
        this.description = description;
        this.sessions = sessions;
    }

    /**
     * Business identifier of this entity is its name
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
