package pl.lodz.p.it.ssbd2024.ssbd01.mok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository  extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
