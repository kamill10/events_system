package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountHistory;

import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AccountMokHistoryRepository extends JpaRepository<AccountHistory, UUID> {

    @PreAuthorize("permitAll()")
    @Override
    AccountHistory saveAndFlush(AccountHistory accountHistory);

    List<AccountHistory> findAllByAccount_Username(String username);

}
