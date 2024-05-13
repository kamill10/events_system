package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractCredentialChange;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


public interface GenericChangeCredentialTokenRepository<T extends AbstractCredentialChange> extends JpaRepository<T, UUID> {
    Optional<T> findByToken(String token);

    void deleteAllByExpirationDateBefore(LocalDateTime expirationDate);

    void deleteByToken(String token);
}
