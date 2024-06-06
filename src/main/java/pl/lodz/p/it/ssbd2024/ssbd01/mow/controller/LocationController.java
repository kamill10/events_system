package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.LocationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.LocationDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.LocationService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Page<GetLocationDTO>> getAllLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "id") String key
    ) {
        PageUtils pageUtils = new PageUtils(page, size, direction, key);
        Page<GetLocationDTO> getLocationDTOPage = LocationDTOConverter.locationDTOPage(locationService.getAllLocations(pageUtils));
        return ResponseEntity.status(HttpStatus.OK).body(getLocationDTOPage);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> deleteLocation(@PathVariable UUID id) throws LocationNotFoundException {
        locationService.deleteLocation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<GetLocationDTO> createLocation(@RequestBody CreateLocationDTO createLocationDTO) {
        GetLocationDTO createdLocation =
                LocationDTOConverter.toGetLocationDTO(locationService.createLocation(LocationDTOConverter.toLocation(createLocationDTO)));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<GetLocationDTO> updateLocation(@RequestHeader(HttpHeaders.IF_MATCH) String eTagReceived, @PathVariable UUID id, @RequestBody
    UpdateLocationDTO updateLocationDTO) throws LocationNotFoundException, OptLockException {
        var location = locationService.updateLocation(id, LocationDTOConverter.toLocation(updateLocationDTO), eTagReceived);
        String eTag = ETagBuilder.buildETag(location.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.IF_MATCH, eTag)
                .body(LocationDTOConverter.toGetLocationDTO(location));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<GetLocationDTO> getLocation(@PathVariable UUID id) throws LocationNotFoundException {
        Location location = locationService.getLocationById(id);
        String eTag = ETagBuilder.buildETag(location.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG, eTag)
                .body(LocationDTOConverter.toGetLocationDTO(locationService.getLocationById(id)));
    }


}
