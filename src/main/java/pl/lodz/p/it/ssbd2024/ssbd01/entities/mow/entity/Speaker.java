package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.entity;

import jakarta.persistence.*;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.util.UUID;

@Data
@Entity
public class Speaker extends AbstractEntity {

    @Column(nullable = false,length = 32)
    private String firstName;
    @Column(nullable = false,length = 64)
    private String lastName;
}
