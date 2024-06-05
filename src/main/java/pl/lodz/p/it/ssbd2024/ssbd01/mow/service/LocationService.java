package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.LocationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetLocationPageDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.LocationRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.RoomRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Page<Location> getAllLocations(GetLocationPageDTO getLocationPageDTO) {
        Sort sort = getLocationPageDTO.buildSort();
        Pageable pageable = PageRequest.of(getLocationPageDTO.page(), getLocationPageDTO.elementPerPage(), sort);
        return locationRepository.findAllByIsActiveTrue(pageable);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteLocation(UUID id) throws LocationNotFoundException {
        Location location = locationRepository.findById(id).orElseThrow(() -> new LocationNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND));
        location.setIsActive(false);
        for (var room : location.getRooms()) {
            room.setIsActive(false);
        }
        locationRepository.saveAndFlush(location);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void createLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateLocation(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
