package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AccountMokRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);

    @PreAuthorize("permitAll()")
    @Override
    Account saveAndFlush(Account account);

    @PreAuthorize("permitAll()")
    @Override
    Optional<Account> findById(UUID id);

    @PreAuthorize("permitAll()")
    Optional<Account> findByEmail(String email);

    List<Account> findAccountByRolesContains(Role role);

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    List<Account> findByNonLockedFalseAndLockedUntilBefore(LocalDateTime dateTime);

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    List<Account> findByNonLockedTrueAndLastSuccessfulLoginBefore(LocalDateTime dateTime);


    @Query("SELECT a FROM Account a WHERE lower(a.firstName) LIKE %:phrase% OR lower(a.lastName) LIKE %:phrase%")
    List<Account> findAllByPhrase(String phrase);
}
