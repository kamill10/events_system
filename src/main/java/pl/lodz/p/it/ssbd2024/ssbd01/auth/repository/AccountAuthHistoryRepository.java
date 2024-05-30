package pl.lodz.p.it.ssbd2024.ssbd01.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountHistory;

import java.util.UUID;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Transactional(propagation = Propagation.MANDATORY)
public interface AccountAuthHistoryRepository extends JpaRepository<AccountHistory, UUID> {

    @PreAuthorize("permitAll()")
    @Override
    AccountHistory saveAndFlush(AccountHistory accountHistory);

}
