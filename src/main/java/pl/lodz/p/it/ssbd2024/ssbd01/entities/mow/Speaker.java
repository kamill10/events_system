package pl.lodz.p.it.ssbd2024.ssbd01.entities.mow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

@Data
@Entity
public class Speaker extends AbstractEntity {

    @Column(nullable = false, length = 32)
    @NotBlank
    @Size(max = 32)
    private String firstName;

    @Column(nullable = false, length = 64)
    @NotBlank
    @Size(max = 64)
    private String lastName;
}
