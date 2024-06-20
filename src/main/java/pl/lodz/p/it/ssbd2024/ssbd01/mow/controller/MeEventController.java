package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetEventDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetTicketDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetTicketDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.*;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.EventDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.TicketDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.MeEventService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/events/me")
@RequiredArgsConstructor
public class MeEventController {

    private final MeEventService meEventService;

    @PostMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<String> signUpForSession(@RequestHeader("If-Match") String eTagReceived, @PathVariable UUID id)
            throws AppException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(meEventService.signUpForSession(id, eTagReceived).getSession().getName());
    }

    @GetMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<GetTicketDetailedDTO> getSession(@PathVariable UUID id) throws AppException {
        Ticket ticket = meEventService.getSession(id);
        String eTag = ETagBuilder.buildETag(ticket.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.ETAG, eTag).body(TicketDTOConverter.toTicketDetailedDTO(ticket));
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
    public ResponseEntity<Page<GetEventDTO>> getMyHistoricalEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "id") String key
    ) {
        PageUtils pageUtils = new PageUtils(page, size, direction, key);
        meEventService.getMyHistoricalEvents(pageUtils);
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (account.getLanguage().getLanguageCode().equals("pl-PL")) {
            return ResponseEntity.status(HttpStatus.OK).body(EventDTOConverter.eventPlDTOPage(meEventService.getMyHistoricalEvents(pageUtils)));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(EventDTOConverter.eventEnDTOPage(meEventService.getMyHistoricalEvents(pageUtils)));
        }
    }

    @DeleteMapping("/session/{id}")
    @PreAuthorize("hasRole('ROLE_PARTICIPANT')")
    public ResponseEntity<?> signOutOfSession(@PathVariable UUID id, @RequestHeader("If-Match") String eTag)
            throws AppException {
        meEventService.signOutOfSession(id, eTag);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
