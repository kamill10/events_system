package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.*;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

@Data
@Entity
public class Room extends AbstractEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, length = 4)
    private Integer maxCapacity;
}
