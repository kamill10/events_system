package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateSessionDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.GetParticipantDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSessionDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSessionForListDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateSessionDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.ParticipantDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.SessionDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.SessionService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;


    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<GetSessionForListDTO>> getAllSessions() {
        List<GetSessionForListDTO> sessions = SessionDTOConverter.getSessionsForListDTO(sessionService.getSessions());
        return ResponseEntity.status(HttpStatus.OK).body(sessions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<GetSessionDetailedDTO> getSession(@PathVariable UUID id)
            throws SessionNotFoundException {
        Session session = sessionService.getSession(id);
        // For signing at session we don't want to check if whole object has changed
        // We don't care about version, we can allow for aggregate to change, we just cant allow it to be negative
        String etag = ETagBuilder.buildETag(
                session.getName(), session.getStartTime().toString(), session.getEndTime().toString()
        );
        GetSessionDetailedDTO sessionDetailedDTO = SessionDTOConverter.toGetDetailedSession(session);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, etag).body(sessionDetailedDTO);
    }

    @GetMapping("/manager/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<GetSessionDetailedDTO> getSessionForManager(@PathVariable UUID id)
            throws SessionNotFoundException {
        Session session = sessionService.getSession(id);
        String etag = ETagBuilder.buildETag(String.valueOf(session.getVersion()));
        GetSessionDetailedDTO sessionDetailedDTO = SessionDTOConverter.toGetDetailedSession(session);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, etag).body(sessionDetailedDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateSession(@PathVariable UUID id,
                                           @RequestHeader(HttpHeaders.IF_MATCH) String etag,
                                           @RequestBody @Valid UpdateSessionDTO updateSessionDTO) throws
            OptLockException,
            SessionsExistOutsideRangeException,
            SessionStartDateInPast,
            SessionNotFoundException,
            SessionStartDateAfterEndDateException, RoomNotFoundException, SpeakerNotFoundException, RoomIsBusyException, RoomSeatsExceededException,
            SpeakerIsBusyException, EntityIsUnmodifiableException {
        Session session = SessionDTOConverter.getSession(updateSessionDTO);
        sessionService.updateSession(id, etag, session, updateSessionDTO.speakerId(), updateSessionDTO.roomId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createSession(@RequestBody @Valid CreateSessionDTO createSessionDTO) throws
            SessionStartDateInPast, SessionStartDateAfterEndDateException, RoomNotFoundException, SpeakerNotFoundException, EventNotFoundException,
            RoomIsBusyException, RoomSeatsExceededException, SpeakerIsBusyException, SessionsExistOutsideRangeException {
        Session session = SessionDTOConverter.getSession(createSessionDTO);
        String newSessionId =
                sessionService.createSession(session, createSessionDTO.eventId(), createSessionDTO.speakerId(), createSessionDTO.roomId());
        return ResponseEntity.status(HttpStatus.OK).body(newSessionId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> cancelSession(@PathVariable UUID id, @RequestHeader(HttpHeaders.IF_MATCH) String etag)
            throws SessionNotFoundException, OptLockException, SessionAlreadyCanceledException {
        sessionService.cancelSession(id, etag);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/{id}/participants")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<GetParticipantDTO>> getParticipants(@PathVariable UUID id) {
        List<GetParticipantDTO> particpantDTOs =
                sessionService.getSessionParticipants(id).stream().map(ParticipantDTOConverter::getParticipantDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(particpantDTOs);
    }
}