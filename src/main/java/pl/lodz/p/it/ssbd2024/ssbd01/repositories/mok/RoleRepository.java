package pl.lodz.p.it.ssbd2024.ssbd01.repositories.mok;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Role;

import java.util.UUID;

public interface RoleRepository  extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
