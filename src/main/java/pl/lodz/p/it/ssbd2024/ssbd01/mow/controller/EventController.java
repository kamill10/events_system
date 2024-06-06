package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.EventAlreadyCancelledException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.EventNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.EventDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.EventService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * Method for selecting all events that have not finished yet.
     * @return all events happening in the future, meaning.
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<GetEventDTO>> getAllNonPastEvents() {
        var events = eventService.getAllNotEndedEvents();
        var eventsDTO = events.stream()
                .map(EventDTOConverter::getEventDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(eventsDTO);
    }

    @GetMapping("/sessions/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getEventSessions(@PathVariable UUID id) {
        eventService.getEventSessions(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateSession(@PathVariable UUID id) {
        eventService.updateSession(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/session")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createSession() {
        eventService.createSession();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> cancelSession(@PathVariable UUID id) {
        eventService.cancelSession(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateEvent(@PathVariable UUID id) {
        eventService.updateEvent(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createEvent() {
        eventService.createEvent();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> cancelEvent(@PathVariable UUID id) throws EventNotFoundException, EventAlreadyCancelledException {
        eventService.cancelEvent(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/mail")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> sendMail(@RequestBody String placeHolder) {
        eventService.sendMail(placeHolder);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}