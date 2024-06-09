package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSearchSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Speaker;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.SpeakerDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.SpeakerService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/speakers")
@RequiredArgsConstructor
public class SpeakerController {

    private final SpeakerService speakerService;


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getSpeaker(@PathVariable UUID id) throws AppException {
        var speaker = speakerService.getSpeaker(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.IF_MATCH, ETagBuilder.buildETag(String.valueOf(speaker.getVersion())))
                .body(SpeakerDTOConverter.convertToDTO(speaker));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getAllSpeakers(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size,
                                                    @RequestParam(defaultValue = "asc") String direction,
                                                    @RequestParam(defaultValue = "id") String key) {
        PageUtils pageUtils = new PageUtils(page, size, direction, key);
        return ResponseEntity.ok()
                .body(speakerService.getAllSpeakers(pageUtils)
                        .map(SpeakerDTOConverter::convertToDTO));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createSpeaker(@RequestBody CreateSpeakerDTO createSpeakerDTO) {
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

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> searchSpeakers(@RequestBody GetSearchSpeakerDTO getSearchSpeakerDTO) {
        return ResponseEntity.ok()
                .body(speakerService.searchSpeakers(getSearchSpeakerDTO.firstName(), getSearchSpeakerDTO.lastName())
                        .stream()
                        .map(SpeakerDTOConverter::convertToDTO));
    }

}
