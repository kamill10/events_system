package pl.lodz.p.it.ssbd2024.ssbd01.entities.util;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.math.BigInteger;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(name = "id", columnDefinition = "UUID", updatable = false)
    private UUID id;

    @Version
    private Integer version;
}