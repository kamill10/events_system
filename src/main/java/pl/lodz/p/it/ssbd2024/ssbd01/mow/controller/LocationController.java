package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.LocationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.LocationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> getAllLocations() {
        locationService.getAllLocations();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> deleteLocation(@PathVariable UUID id) throws LocationNotFoundException {
        locationService.deleteLocation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> createLocation() {
        locationService.createLocation();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateLocation(@PathVariable UUID id) {
        locationService.updateLocation(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
