package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.ControlledEntity;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Room extends ControlledEntity {

    @Column(nullable = false, updatable = false, unique = true)
    @NotBlank
    @Size(min = 3, max = 32)
    private String name;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(nullable = false, updatable = false, name = "location_id")
    @NotNull
    private Location location;

    @Column(nullable = false, length = 4)
    @NotNull
    @Min(1)
    @Max(1000)
    private Integer maxCapacity;

    public Room(String name, Location location, Integer maxCapacity) {
        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;
    }

    /**
     * Business identifier of room is its name and location
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return Objects.equals(name, room.name) && Objects.equals(location, room.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }
}
