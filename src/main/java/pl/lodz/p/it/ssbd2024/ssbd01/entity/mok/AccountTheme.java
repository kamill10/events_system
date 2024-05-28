package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.ThemeEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_theme")
public class AccountTheme extends AbstractEntity {

    @Column(nullable = false, unique = true, updatable = false, length = 15)
    @NotBlank
    @Size(max = 15)
    private String theme;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountTheme accountTheme1 = (AccountTheme) o;
        return Objects.equals(theme, accountTheme1.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theme);
    }
}
