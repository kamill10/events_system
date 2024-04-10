package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.ControlledEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Location extends ControlledEntity {

    @Column(nullable = false, unique = true, updatable = false)
    @NotBlank
    @Size(min = 3, max = 32)
    @NotNull
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    /**
     * Business identifier of location is its name
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location location)) return false;
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
