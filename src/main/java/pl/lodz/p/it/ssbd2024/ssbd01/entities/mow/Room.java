package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.ControlledEntity;

@Data
@Entity
public class Room extends ControlledEntity {

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 32)
    private String name;

    @Column(nullable = false, length = 4)
    @NotNull
    @Min(0)
    @Max(1000)
    private Integer maxCapacity;
}
