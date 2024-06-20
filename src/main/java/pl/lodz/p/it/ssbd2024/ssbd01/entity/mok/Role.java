package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role extends AbstractEntity {

    @Column(nullable = false, unique = true, updatable = false, length = 32)
    @NotBlank(message = ExceptionMessages.INCORRECT_ROLE_NAME)
    @Size(max = 32, message = ExceptionMessages.INCORRECT_ROLE_NAME)
    @Enumerated(EnumType.STRING)
    private AccountRoleEnum name;


    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role role)) {
            return false;
        }
        return name != null && name.equals(role.getName());
    }

    @Override
    public final int hashCode() {
        if (name != null) {
            return name.hashCode();
        }
        return super.hashCode();
    }
}
