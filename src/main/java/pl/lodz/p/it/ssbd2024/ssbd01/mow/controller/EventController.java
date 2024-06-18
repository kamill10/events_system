package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import com.deepl.api.DeepLException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.GetParticipantDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSessionForListDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Event;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.EventDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.ParticipantDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.SessionDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.EventService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.TranslationUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<GetEventDTO>> getAllNonPastEvents(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) {
        List<Event> events = eventService.getAllNotEndedEvents();
        List<GetEventDTO> eventDTOs = TranslationUtils.resolveEventsLanguage(events, language);
        return ResponseEntity.status(HttpStatus.OK).body(eventDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<GetEventDTO> getEvent(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language, @PathVariable UUID id)
            throws EventNotFoundException {
        Event event = eventService.getEvent(id);
        String etag = ETagBuilder.buildETag(event.getVersion().toString());
        GetEventDTO eventDTO = TranslationUtils.resolveEventLanguage(event, language);
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, etag).body(eventDTO);
    }

    @GetMapping("/sessions/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<GetSessionForListDTO>> getEventSessions(@PathVariable UUID id) {
        var sessionEvents = eventService.getEventSessions(id);
        var sessionEventsDTO = sessionEvents.stream()
                .map(SessionDTOConverter::getSessionForListDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(sessionEventsDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<GetEventDTO> updateEvent(
            @PathVariable UUID id,
            @RequestHeader(HttpHeaders.IF_MATCH) String etag,
            @RequestBody @Valid UpdateEventDTO updateEventDTO) throws
            OptLockException,
            SessionsExistOutsideRangeException,
            EventNotFoundException,
            EventStartDateAfterEndDateException,
            DeepLException,
            InterruptedException,
            EntityIsUnmodifiableException {
        Event event = EventDTOConverter.getEvent(updateEventDTO);
        Event updatedEvent = eventService.updateEvent(id, etag, event);
        return ResponseEntity.status(HttpStatus.OK).body(EventDTOConverter.getEventPlDTO(updatedEvent));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<String> createEvent(@RequestBody @Valid CreateEventDTO createEventDTO)
            throws
            EventStartDateAfterEndDateException,
            DeepLException,
            InterruptedException,
            EventStartDateInPastException {
        Event event = EventDTOConverter.getEvent(createEventDTO);
        String eventId = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> cancelEvent(@PathVariable UUID id, @RequestHeader(HttpHeaders.IF_MATCH) String etag)
            throws EventNotFoundException, EventAlreadyCancelledException, OptLockException {
        eventService.cancelEvent(id, etag);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/session/{id}/participants")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<GetParticipantDTO>> getParticipants(@PathVariable UUID id) {
        List<Account> participants = eventService.getSessionParticipants(id);

        List<GetParticipantDTO> participantDTOs = participants.stream()
                .map(ParticipantDTOConverter::getParticipantDTO)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(participantDTOs);
    }
}