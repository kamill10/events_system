package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.TimeZoneEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.util.TimeZone;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_time_zone")
public class AccountTimeZone extends AbstractEntity {

    @Column(nullable = false, unique = true, updatable = false, length = 15)
    @NotBlank
    @Size(max = 15)
    @Enumerated(EnumType.STRING)
    private TimeZoneEnum timeZoneEnum;


    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(timeZoneEnum.getTimeZone());
    }
}
