package pl.lodz.p.it.ssbd2024.ssbd01.entity.mow;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ControlledEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "location")
@NoArgsConstructor
public class Location extends ControlledEntity {

    @Column(nullable = false, unique = true, updatable = false)
    @NotBlank
    @Size(min = 3, max = 128)
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String street;

    @Column(nullable = false)
    @NotBlank
    private String buildingNumber;

    @Column(nullable = false)
    @NotBlank
    @Pattern(regexp = "\\d{2}-\\d{3}")
    private String postalCode;

    @Column(nullable = false)
    @NotBlank
    private String city;

    @Column(nullable = false)
    @NotBlank
    private String country;

    @Column(nullable = false)
    @NotNull
    private Boolean isActive = true;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "location")
    private List<Room> rooms = new ArrayList<>();

    public Location(String name, String city,String country, String street, String buildingNumber, String postalCode) {
        this.name = name;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location location)) {
            return false;
        }
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
