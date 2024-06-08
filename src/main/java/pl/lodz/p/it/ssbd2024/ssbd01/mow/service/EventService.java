package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.EventAlreadyCancelledException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.EventNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.EventRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.SessionRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.TicketRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public List<Event> getAllNotEndedEvents() {
        var currentDate = LocalDate.now();
        return eventRepository.getByEndDateAfter(currentDate);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public List<Session> getEventSessions(UUID id) {
        return sessionRepository.getByEventId(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateSession(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void createSession() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void cancelSession(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateEvent(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void createEvent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void cancelEvent(UUID id) throws EventNotFoundException, EventAlreadyCancelledException {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));

        if (!event.getIsNotCanceled()) {
            throw new EventAlreadyCancelledException(ExceptionMessages.EVENT_ALREADY_CANCELLED);
        }

        event.setIsNotCanceled(false);

        List<Account> accounts = event.getSessions()
                .stream()
                .peek(session -> session.setIsActive(false))
                .flatMap(session -> session.getTickets().stream())
                .peek(ticket -> ticket.setIsNotCancelled(false))
                .collect(Collectors.groupingBy(Ticket::getAccount))
                .keySet()
                .stream()
                .toList();

        //TODO: send mail to all accounts

        eventRepository.saveAndFlush(event);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void sendMail(String placeHolder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
