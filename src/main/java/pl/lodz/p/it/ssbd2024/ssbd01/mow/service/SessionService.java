package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import com.deepl.api.DeepLException;
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
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.TranslationUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.util.mail.MailService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final SpeakerRepository speakerRepository;
    private final RoomRepository roomRepository;
    private final ConfigurationProperties config;
    private final MailService mailService;


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateSession(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String createSession(Session session, UUID eventId, UUID speakerId, UUID roomId) throws SessionStartDateAfterEndDateException, SessionStartDateInPast, EventNotFoundException, RoomNotFoundException, SpeakerNotFoundException {
        if (session.getStartTime().isBefore(LocalDateTime.now())) {
            throw new SessionStartDateInPast("KOTWICA");
        }
        if (session.getStartTime().isAfter(session.getEndTime())) {
            throw new SessionStartDateAfterEndDateException("KOTWICA");
        }

        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));
        session.setEvent(event);
        var speaker = speakerRepository.findById(speakerId)
                .orElseThrow(() -> new SpeakerNotFoundException(ExceptionMessages.SPEAKER_NOT_FOUND));
        session.setSpeaker(speaker);
        var room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));
        session.setRoom(room);
        sessionRepository.saveAndFlush(session);
        return session.getId().toString();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void cancelSession(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Session getSession(UUID id) throws SessionNotFoundException {
        return sessionRepository.findById(id).orElseThrow(() -> new SessionNotFoundException(ExceptionMessages.SESSION_NOT_FOUND));
    }
}
