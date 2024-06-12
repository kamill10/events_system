package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.EventRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.SessionRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.TicketRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.Mail;
import pl.lodz.p.it.ssbd2024.ssbd01.util.RunAs;
import pl.lodz.p.it.ssbd2024.ssbd01.util.TranslationUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.mail.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final ConfigurationProperties config;
    private final MailService mailService;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public List<Event> getAllNotEndedEvents() {
        var currentDateTime = LocalDateTime.now();
        return eventRepository.getByEndDateAfter(currentDateTime);
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
    public Event updateEvent(UUID id, String etag, Event event) throws
            EventNotFoundException,
            OptLockException,
            SessionsExistOutsideRangeException,
            EventStartDateAfterEndDateException,
            DeepLException,
            InterruptedException,
            EventStartDateInPast {
        Event databaseEvent = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));

        if (!ETagBuilder.isETagValid(etag, String.valueOf(databaseEvent.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }

        LocalDateTime newEventStartTime = event.getStartDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime newEventEndTime = event.getEndDate().withHour(23).withMinute(59).withSecond(59);
        if (newEventStartTime.getDayOfMonth() - LocalDate.now().getDayOfMonth() < 0) {
            throw new EventStartDateInPast(ExceptionMessages.EVENT_START_IN_PAST);
        }
        List<Session> sessionsOutsideRange = sessionRepository.findSessionsOutsideRange(id, newEventStartTime, newEventEndTime);

        if (!sessionsOutsideRange.isEmpty()) {
            throw new SessionsExistOutsideRangeException(ExceptionMessages.SESSIONS_OUTSIDE_RANGE);
        }

        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new EventStartDateAfterEndDateException(ExceptionMessages.EVENT_START_AFTER_END);
        }

        databaseEvent.setName(event.getName());
        databaseEvent.setStartDate(event.getStartDate());
        databaseEvent.setEndDate(event.getEndDate());
        databaseEvent.setDescriptionPL(event.getDescriptionPL());

        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TranslationUtils.translateEvent(databaseEvent, account.getLanguage().getLanguageCode(), config.getDeepl());

        return eventRepository.saveAndFlush(databaseEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String createEvent(Event event) throws
            EventStartDateAfterEndDateException,
            DeepLException,
            InterruptedException,
            EventStartDateInPast {
        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new EventStartDateAfterEndDateException(ExceptionMessages.EVENT_START_AFTER_END);
        }
        LocalDateTime newEventStartTime = event.getStartDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime newEventEndTime = event.getEndDate().withHour(23).withMinute(59).withSecond(59);
        if (newEventStartTime.getDayOfMonth() - LocalDate.now().getDayOfMonth() < 0) {
            throw new EventStartDateInPast(ExceptionMessages.EVENT_START_IN_PAST);
        }
        event.setStartDate(newEventStartTime);
        event.setEndDate(newEventEndTime);

        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TranslationUtils.translateEvent(event, account.getLanguage().getLanguageCode(), config.getDeepl());

        eventRepository.saveAndFlush(event);
        return event.getId().toString();
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @Retryable(
            retryFor = {UnexpectedRollbackException.class},
            maxAttemptsExpression = "${transaction.retry.max}",
            backoff = @Backoff(delayExpression = "${transaction.retry.delay}")
    )
    public void cancelEvent(UUID id, String etag) throws EventNotFoundException, EventAlreadyCancelledException, OptLockException {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));

        if (!ETagBuilder.isETagValid(etag, String.valueOf(event.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }

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

        accounts.forEach(account -> {
            RunAs.runAsSystem(() -> mailService.sendEmailOnEventCancel(account, event.getName()));
        });

        eventRepository.saveAndFlush(event);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public Event getEvent(UUID id) throws EventNotFoundException {
        return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Account> getSessionParticipants(UUID id) {
        List<Ticket> tickets = ticketRepository.findBySession_Id(id);

        return tickets.stream()
                .map(Ticket::getAccount)
                .toList();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public Session getSession(UUID id) {
        return sessionRepository.findById(id).orElseThrow(() -> new NoSuchElementException(ExceptionMessages.SESSION_NOT_FOUND));
    }
}
