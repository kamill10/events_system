package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetTicketDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetTicketDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.TicketAlreadyCancelledException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.TicketNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.TicketDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.MeEventService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/events/me")
@RequiredArgsConstructor
public class MeEventController {

    private final MeEventService meEventService;

    @PostMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> signUpForSession(@PathVariable UUID id)
            throws SessionNotFoundException, SessionNotActiveException, MaxSeatsOfSessionReachedException, AlreadySignUpException {
        meEventService.signUpForSession(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<GetTicketDetailedDTO> getSession(@PathVariable UUID id) throws TicketNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(TicketDTOConverter.toTicketDetailedDTO(meEventService.getSession(id)));
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<Page<GetTicketDTO>> getMyFutureAndPresentSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "id") String key
    ) {
        PageUtils pageUtils = new PageUtils(page, size, direction, key);
        Page<Ticket> tickets = meEventService.getMyFutureAndPresentSessions(pageUtils);
        return ResponseEntity.status(HttpStatus.OK).body(TicketDTOConverter.ticketDTOPage(tickets));
    }

    @GetMapping("/past-sessions")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<Page<GetTicketDTO>> getMyHistoricalSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "id") String key
    ) {
        PageUtils pageUtils = new PageUtils(page, size, direction, key);
        Page<Ticket> tickets = meEventService.getMyHistoricalSessions(pageUtils);
        return ResponseEntity.status(HttpStatus.OK).body(TicketDTOConverter.ticketDTOPage(tickets));
    }

    @GetMapping("/historical-events")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> getMyHistoricalEvents() {
        meEventService.getMyHistoricalEvents();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> signOutFromSession(@PathVariable UUID id) throws TicketNotFoundException, TicketAlreadyCancelledException {
        meEventService.signOutFromSession(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
