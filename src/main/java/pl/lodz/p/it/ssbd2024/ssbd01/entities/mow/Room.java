package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

@Data
@Entity
public class Room extends AbstractEntity {

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 32)
    private String name;

    @Column(nullable = false, length = 4)
    @Range(max = 1024)
    @NotNull
    private Integer maxCapacity;
}
