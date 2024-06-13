package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final SpeakerRepository speakerRepository;
    private final RoomRepository roomRepository;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {Exception.class},
            timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateSession(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {Exception.class},
            timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String createSession(Session session, UUID eventId, UUID speakerId, UUID roomId)
            throws SessionStartDateAfterEndDateException,
            SessionStartDateInPast,
            EventNotFoundException,
            RoomNotFoundException,
            SpeakerNotFoundException,
            RoomSeatsExceededException,
            RoomIsBusyException,
            SpeakerIsBusyException {
        if (session.getStartTime().isBefore(LocalDateTime.now())) {
            throw new SessionStartDateInPast("KOTWICA");
        }
        if (session.getStartTime().isAfter(session.getEndTime())) {
            throw new SessionStartDateAfterEndDateException("KOTWICA");
        }

        var event = eventRepository
                .findByIdAndIsNotCanceledTrue(eventId)
                .orElseThrow(() -> new EventNotFoundException(ExceptionMessages.EVENT_NOT_FOUND));
        session.setEvent(event);
        var speaker = speakerRepository
                .findById(speakerId)
                .orElseThrow(() -> new SpeakerNotFoundException(ExceptionMessages.SPEAKER_NOT_FOUND));
        session.setSpeaker(speaker);
        var room = roomRepository
                .findByIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));

        List<Session> speakerOverlappingSessions = sessionRepository.findSpeakerSessionsInRange(
                speakerId,
                session.getStartTime(),
                session.getEndTime());

        if (!speakerOverlappingSessions.isEmpty()) {
            throw new SpeakerIsBusyException(ExceptionMessages.SPEAKER_IS_BUSY);
        }

        if (session.getMaxSeats() > room.getMaxCapacity()) {
            throw new RoomSeatsExceededException(ExceptionMessages.ROOM_SEATS_EXCEEDED);
        }

        List<Session> overlappingSessions = sessionRepository.findSessionsInsideRangeAtRoom(
                roomId,
                session.getStartTime(),
                session.getEndTime());

        if (!overlappingSessions.isEmpty()) {
            throw new RoomIsBusyException(ExceptionMessages.ROOM_BUSY);
        }

        session.setRoom(room);

        sessionRepository.saveAndFlush(session);
        return session.getId().toString();
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {Exception.class},
            timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void cancelSession(UUID id, String etag)
            throws SessionNotFoundException, OptLockException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {Exception.class},
            timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Account> getSessionParticipants(UUID id) {
        List<Ticket> tickets = ticketRepository.findBySession_Id(id);

        return tickets.stream().map(Ticket::getAccount).toList();
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {Exception.class},
            timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public Session getSession(UUID id) throws SessionNotFoundException {
        return sessionRepository
                .findById(id)
                .orElseThrow(() -> new SessionNotFoundException(ExceptionMessages.SESSION_NOT_FOUND));
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {Exception.class},
            timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public List<Session> getSessions() {
        return sessionRepository.findAll();
    }
}
