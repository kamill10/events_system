package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.util.UUID;

@Data
@Entity
public class Room extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Integer maxCapacity;
}
