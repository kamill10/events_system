package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.SpeakerDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.SpeakerService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/speakers")
@RequiredArgsConstructor
public class SpeakerController {

    private final SpeakerService speakerService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getAllSpeakers() {
        return ResponseEntity.ok()
                .body(speakerService.getAllSpeakers()
                        .stream()
                        .map(SpeakerDTOConverter::convertToDTO)
                        .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getSpeaker(@PathVariable UUID id) throws AppException {
        var speaker = speakerService.getSpeaker(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.IF_MATCH, ETagBuilder.buildETag(String.valueOf(speaker.getVersion())))
                .body(SpeakerDTOConverter.convertToDTO(speaker));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createSpeaker() {
        speakerService.createSpeaker();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateSpeaker(@PathVariable UUID id) {
        speakerService.updateSpeaker(id);
        return ResponseEntity.ok().build();
    }

}
