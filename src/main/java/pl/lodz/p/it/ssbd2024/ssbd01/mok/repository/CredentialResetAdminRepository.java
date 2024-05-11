package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.CredentialChangeByAdmin;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface CredentialResetAdminRepository extends JpaRepository<CredentialChangeByAdmin, UUID> {
    Optional<CredentialChangeByAdmin> findByToken(String token);

    void deleteAllByExpirationDateBefore(LocalDateTime expirationDate);

    void deleteByToken(String token);
}
