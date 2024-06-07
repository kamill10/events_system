package pl.lodz.p.it.ssbd2024.ssbd01.entity.mow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ControlledEntity;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"firstName", "lastName"}),
        name = "speaker"
)
public class Speaker extends ControlledEntity {

    @Column(nullable = false, length = 32)
    @NotBlank
    @Size(min = 2, max = 32)
    private String firstName;

    @Column(nullable = false, length = 64)
    @NotBlank
    @Size(min = 2, max = 64)
    private String lastName;

    public Speaker(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Speaker speaker)) {
            return false;
        }
        return Objects.equals(firstName, speaker.firstName) && Objects.equals(lastName, speaker.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}
