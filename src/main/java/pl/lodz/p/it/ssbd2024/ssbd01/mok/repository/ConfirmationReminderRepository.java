package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ConfirmationReminder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ConfirmationReminderRepository extends JpaRepository<ConfirmationReminder, UUID> {

    List<ConfirmationReminder> findByReminderDateBefore(LocalDateTime now);

    void deleteByAccount(Account account);
}
