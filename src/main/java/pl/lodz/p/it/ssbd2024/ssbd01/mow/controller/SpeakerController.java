package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.create.CreateSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Speaker;
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
    public ResponseEntity<?> createSpeaker(CreateSpeakerDTO createSpeakerDTO) {
        return ResponseEntity
                .ok(SpeakerDTOConverter
                        .convertToDTO(speakerService
                                        .createSpeaker(new Speaker(
                                                createSpeakerDTO.firstName(),
                                                createSpeakerDTO.lastName()))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateSpeaker(@PathVariable UUID id,
                                           @RequestHeader(HttpHeaders.IF_MATCH) String eTagReceived,
                                           @RequestBody UpdateSpeakerDTO updateSpeakerDTO) throws AppException {
        return ResponseEntity
                .ok(SpeakerDTOConverter
                        .convertToDTO(speakerService.updateSpeaker(id,
                                                                   new Speaker(
                                                                   updateSpeakerDTO.firstName(),
                                                                   updateSpeakerDTO.lastName()),
                                                                   eTagReceived)));
    }

}
