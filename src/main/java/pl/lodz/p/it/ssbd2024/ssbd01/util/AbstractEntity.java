package pl.lodz.p.it.ssbd2024.ssbd01.util;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Column(name = "id", columnDefinition = "UUID", updatable = false)
    private UUID id;

    @Version
    @Column(nullable = false)
    private Long version;
}