package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_MANAGER')")
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    @Override
    Optional<Ticket> findById(UUID id);

    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    @Query("SELECT t FROM Ticket t WHERE t.account.id = :accountId AND t.session.endTime >= :now")
    Page<Ticket> findAllByAccountIdAndEndTimeAfterNow(UUID accountId, LocalDateTime now, Pageable pageable);

    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    @Query("SELECT t FROM Ticket t WHERE t.account.id = :accountId AND t.session.endTime < :now")
    Page<Ticket> findAllByAccountIdAndEndTimeBeforeNow(UUID accountId, LocalDateTime now, Pageable pageable);

    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    @Override
    Ticket saveAndFlush(Ticket ticket);

    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    List<Ticket> findBySession(Session session);

    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    Optional<Ticket> findBySessionAndAccount(Session session, Account account);
}
