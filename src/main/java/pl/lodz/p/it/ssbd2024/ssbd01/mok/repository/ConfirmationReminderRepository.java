package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountConfirmation;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ConfirmationReminder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface ConfirmationReminderRepository extends JpaRepository<ConfirmationReminder, UUID> {

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    List<ConfirmationReminder> findByReminderDateBefore(LocalDateTime now);

    @PreAuthorize("permitAll()")
    void deleteByAccount(Account account);

    @PreAuthorize("permitAll()")
    @Override
    ConfirmationReminder saveAndFlush(ConfirmationReminder accountConfirmation);
}
