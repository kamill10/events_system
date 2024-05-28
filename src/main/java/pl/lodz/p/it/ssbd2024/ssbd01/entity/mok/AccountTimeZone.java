package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.util.TimeZone;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_time_zone")
public class AccountTimeZone extends AbstractEntity {

    @Column(nullable = false, unique = true, updatable = false, length = 25)
    @NotBlank
    @Size(max = 25)
    private String timeZone;


    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(timeZone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountTimeZone that)) {
            return false;
        }

        return getTimeZone().equals(that.getTimeZone());
    }

    @Override
    public int hashCode() {
        return getTimeZone().hashCode();
    }
}
