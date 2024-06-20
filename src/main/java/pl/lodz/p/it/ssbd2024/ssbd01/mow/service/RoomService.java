package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.LocationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.RoomNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.LocationRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.RoomRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public Page<Room> getAllLocationRooms(UUID locationId, PageUtils pageUtils) {
        Pageable pageable = pageUtils.buildPageable();
        return roomRepository.findAllByLocationIdAndIsActiveTrue(locationId, pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Room> getAllLocationRooms(UUID locationId) {
        return roomRepository.findAllByLocationIdAndIsActiveTrue(locationId);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Room> getAllDeletedRooms(UUID locationId) {
        return roomRepository.findAllByLocationIdAndIsActiveFalse(locationId);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("permitAll()")
    public Room getRoomById(UUID roomId) throws AppException {
        return roomRepository.findByIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Retryable(
            retryFor = {UnexpectedRollbackException.class},
            maxAttemptsExpression = "${transaction.retry.max}",
            backoff = @Backoff(delayExpression = "${transaction.retry.delay}")
    )
    public Room createRoom(Room room, UUID locationId) throws AppException {
        var location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND));
        room.setLocation(location);
        return roomRepository.saveAndFlush(room);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Retryable(
            retryFor = {UnexpectedRollbackException.class},
            maxAttemptsExpression = "${transaction.retry.max}",
            backoff = @Backoff(delayExpression = "${transaction.retry.delay}")
    )
    public Room updateRoom(UUID roomId, Room room, String eTagReceived) throws AppException {
        Room roomToUpdate = roomRepository.findByIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTagReceived, String.valueOf(roomToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        roomToUpdate.setName(room.getName());
        roomToUpdate.setMaxCapacity(room.getMaxCapacity());
        return roomRepository.saveAndFlush(roomToUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Retryable(
            retryFor = {UnexpectedRollbackException.class},
            maxAttemptsExpression = "${transaction.retry.max}",
            backoff = @Backoff(delayExpression = "${transaction.retry.delay}")
    )
    public void deleteRoom(UUID roomId, String eTagReceived) throws AppException {
        var room = roomRepository.findByIdAndIsActiveTrue(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTagReceived, String.valueOf(room.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        room.setIsActive(false);
        roomRepository.saveAndFlush(room);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void activateRoom(UUID roomId, String eTagReceived) throws AppException {
        var room = roomRepository.findByIdAndIsActiveFalse(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTagReceived, String.valueOf(room.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        room.setIsActive(true);
        roomRepository.saveAndFlush(room);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Room getDeletedRoomById(UUID roomId) throws AppException {
        return roomRepository.findByIdAndIsActiveFalse(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));
    }
}
