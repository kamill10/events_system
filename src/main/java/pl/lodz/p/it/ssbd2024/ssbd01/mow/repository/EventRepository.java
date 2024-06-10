package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_MANAGER')")
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Override
    @PreAuthorize("permitAll()")
    Optional<Event> findById(UUID id);

    @PreAuthorize("permitAll()")
    List<Event> getByEndDateAfter(LocalDateTime date);

    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    Page<Event> findAllByEndDateBeforeAndSessions_Tickets_Account(LocalDateTime currentDateTime, Account account, Pageable pageable);
}
