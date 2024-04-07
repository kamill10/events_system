package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.util.List;

@Data
@Entity
public class Location extends AbstractEntity {

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 32)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Room> rooms;
}
