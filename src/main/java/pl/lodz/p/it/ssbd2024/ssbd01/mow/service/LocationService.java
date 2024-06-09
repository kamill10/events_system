package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.LocationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.LocationRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.RoomRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Page<Location> getAllLocations(PageUtils pageUtils) {
        Pageable pageable = pageUtils.buildPageable();
        return locationRepository.findAllByIsActiveTrue(pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Page<Location> getAllDeletedLocations(PageUtils pageUtils) {
        Pageable pageable = pageUtils.buildPageable();
        return locationRepository.findAllByIsActiveFalse(pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Location getDeletedLocationById(UUID id) throws LocationNotFoundException {
        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void restoreLocation(UUID id, String eTag) throws LocationNotFoundException, OptLockException {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTag, String.valueOf(location.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        location.setIsActive(true);
        for (var room : location.getRooms()) {
            room.setIsActive(true);
        }
        locationRepository.saveAndFlush(location);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void deleteLocation(UUID id, String eTag) throws LocationNotFoundException, OptLockException {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTag, String.valueOf(location.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        location.setIsActive(false);
        for (var room : location.getRooms()) {
            room.setIsActive(false);
        }
        locationRepository.saveAndFlush(location);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Location createLocation(Location location) {
        return locationRepository.saveAndFlush(location);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Location updateLocation(UUID id, Location location, String eTag) throws LocationNotFoundException, OptLockException {
        Location locationToUpdate = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTag, String.valueOf(locationToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        locationToUpdate.setName(location.getName());
        locationToUpdate.setCity(location.getCity());
        locationToUpdate.setCountry(location.getCountry());
        locationToUpdate.setBuildingNumber(location.getBuildingNumber());
        locationToUpdate.setStreet(location.getStreet());
        locationToUpdate.setPostalCode(location.getPostalCode());
        return locationRepository.saveAndFlush(locationToUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Location getLocationById(UUID id) throws LocationNotFoundException {
        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND));
    }

}
