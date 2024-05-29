package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.EventService;

import java.util.UUID;

@RestController
@RequestMapping("api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        eventService.getAllEvents();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/sessions/{id}")
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
    public ResponseEntity<?> cancelEvent(@PathVariable UUID id) {
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
