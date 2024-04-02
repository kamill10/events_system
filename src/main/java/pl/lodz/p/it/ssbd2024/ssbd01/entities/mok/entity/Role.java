package pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String name;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Role role)) return false;
        return name != null && name.equals(role.getName());
    }

    @Override
    public final int hashCode() {
        if (name != null) return name.hashCode();
        return super.hashCode();
    }
}
