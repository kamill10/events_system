package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.PasswordHistory;

import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, UUID> {

    @PreAuthorize("permitAll()")
    List<PasswordHistory> findPasswordHistoryByAccount_Id(UUID id);

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    void deletePasswordHistoriesByAccount(Account account);

    @PreAuthorize("permitAll()")
    @Override
    PasswordHistory saveAndFlush(PasswordHistory passwordHistory);
}
