package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.*;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.util.List;

@Data
@Entity
public class Location extends AbstractEntity {

    @Column(nullable = false)
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Room> rooms;
}
