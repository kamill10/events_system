package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ChangeMyPassword;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ConfirmationReminder;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.CredentialReset;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


public interface ChangeMyPasswordRepository extends JpaRepository<ChangeMyPassword, UUID> {
    Optional<ChangeMyPassword> findByToken(String token);

    void deleteAllByExpirationDateBefore(LocalDateTime expirationDate);

    void deleteByToken(String token);
}
