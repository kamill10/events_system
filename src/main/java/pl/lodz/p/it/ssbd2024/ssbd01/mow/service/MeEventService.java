package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.EventRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.SessionRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.TicketRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeEventService {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public void signUpForSession(UUID sessionId)
            throws SessionNotFoundException, SessionNotActiveException, AlreadySignUpException, MaxSeatsOfSessionReachedException {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new SessionNotFoundException(ExceptionMessages.SESSION_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        if (!session.getIsActive() || session.getEndTime().isBefore(LocalDateTime.now())) {
            throw new SessionNotActiveException(ExceptionMessages.SESSION_NOT_ACTIVE);
        }
        if (ticketRepository.findBySession(session).stream()
                .anyMatch(ticket -> ticket.getAccount().getId().equals(account.getId()))) {
            throw new AlreadySignUpException(ExceptionMessages.ALREADY_SIGNED_UP);
        }
        if (ticketRepository.findBySession(session).size() >= session.getMaxSeats()) {
            throw new MaxSeatsOfSessionReachedException(ExceptionMessages.MAX_SEATS_REACHED);
        }
        Ticket ticket = new Ticket(account,session);
        ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public Ticket getSession(UUID id) throws TicketNotFoundException {
        return ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(ExceptionMessages.TICKET_NOT_FOUND));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public Page<Ticket> getMyFutureAndPresentSessions(PageUtils pageUtils) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = pageUtils.buildPageable();
        return ticketRepository.findAllByAccountIdAndEndTimeAfterNow(account.getId(), LocalDateTime.now(), pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public Page<Ticket> getMyPastSessions(PageUtils pageUtils) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = pageUtils.buildPageable();
        return ticketRepository.findAllByAccountIdAndEndTimeBeforeNow(account.getId(), LocalDateTime.now(), pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public void getMyHistoricalEvents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public void signOutFromSession(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
