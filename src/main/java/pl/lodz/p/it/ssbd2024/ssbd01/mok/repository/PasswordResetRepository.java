package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.PasswordReset;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {
    Optional<PasswordReset> findByToken(String token);
}
