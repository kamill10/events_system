package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountConfirmation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface AccountConfirmationRepository extends JpaRepository<AccountConfirmation, UUID> {

    Optional<AccountConfirmation> findByToken(String token);

    List<AccountConfirmation> findByExpirationDateBefore(LocalDateTime now);

    Optional<AccountConfirmation> findByAccount_Id(UUID id);

}
