package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.EventRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.SessionRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.TicketRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeEventService {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public void signUpForSession(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public void getMySessions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public void getMyHistoricalEvents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public void getMyHistoricalSessions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public void signOutFromSession(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
