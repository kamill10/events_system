package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import com.deepl.api.DeepLException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateSessionDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.GetParticipantDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSessionForListDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.EventDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.ParticipantDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.SessionDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.EventService;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.SessionService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.TranslationUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final EventService eventService;
    private final SessionService sessionService;


    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<GetEventDTO>> getAllNonPastEvents(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) {
        List<Event> events = eventService.getAllNotEndedEvents();
        List<GetEventDTO> eventDTOs = TranslationUtils.resolveEventsLanguage(events, language);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<GetEventDTO> getSession(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language, @PathVariable UUID id)
            throws SessionNotFoundException {
        Session session = sessionService.getSession(id);
        String etag = ETagBuilder.buildETag(session.getVersion().toString());
        //todo
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, etag).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateSession(@PathVariable UUID id) {
        sessionService.updateSession(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createSession(@RequestBody CreateSessionDTO createSessionDTO) throws SessionStartDateInPast, SessionStartDateAfterEndDateException, RoomNotFoundException, SpeakerNotFoundException, EventNotFoundException {
        Session session = SessionDTOConverter.getSession(createSessionDTO);
        String newSessionId = sessionService.createSession(session, createSessionDTO.eventId(), createSessionDTO.speakerId(), createSessionDTO.roomId());
        return ResponseEntity.status(HttpStatus.OK).body(newSessionId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> cancelSession(@PathVariable UUID id) {
        sessionService.cancelSession(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/session/{id}/participants")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<GetParticipantDTO>> getParticipants(@PathVariable UUID id) {
        //todo
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}