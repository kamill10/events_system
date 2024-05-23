package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountUnlock;

import java.util.Optional;
import java.util.UUID;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AccountUnlockRepository extends JpaRepository<AccountUnlock, UUID> {

    @PreAuthorize("permitAll()")
    @Override
    AccountUnlock saveAndFlush(AccountUnlock accountUnlock);

    @PreAuthorize("permitAll()")
    Optional<AccountUnlock> findByToken(String token);

    @PreAuthorize("permitAll()")
    @Override
    void delete(AccountUnlock accountUnlock);

}
