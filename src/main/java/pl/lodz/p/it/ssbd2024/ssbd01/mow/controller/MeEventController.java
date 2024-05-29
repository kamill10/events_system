package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.MeEventService;

import java.util.UUID;

@RestController
@RequestMapping("/api/events/me")
@RequiredArgsConstructor
public class MeEventController {

    private final MeEventService meEventService;

    @PostMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> signUpForSession(@PathVariable UUID id) {
        meEventService.signUpForSession(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> getMySessions() {
        meEventService.getMySessions();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/historical-events")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> getMyHistoricalEvents() {
        meEventService.getMyHistoricalEvents();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/historical-sessions")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> getMyHistoricalSessions() {
        meEventService.getMyHistoricalSessions();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> signOutFromSession(@PathVariable UUID id) {
        meEventService.signOutFromSession(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
