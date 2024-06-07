package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_MANAGER')")
public interface EventRepository extends JpaRepository<Event, UUID> {

    @PreAuthorize("permitAll()")
    List<Event> getByEndDateAfter(LocalDate date);

    @Query("SELECT DISTINCT e FROM Event e "
            + "JOIN e.sessions s "
            + "JOIN s.tickets t "
            + "WHERE t.account.id = :accountId "
            + "AND e.endDate < :currentDate")
    List<Event> findAllPastEventsByAccountId(UUID accountId, LocalDate currentDate);

    Page<Event> findById(UUID id, Pageable pageable);

    Page<Event> findAllByIdIn(List<UUID> ids, Pageable pageable);
}
