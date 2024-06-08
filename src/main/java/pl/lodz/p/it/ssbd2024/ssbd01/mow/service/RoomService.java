package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.RoomNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.RoomRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Page<Room> getAllLocationRooms(UUID locationId, PageUtils pageUtils) {
        Pageable pageable = pageUtils.buildPageable();
        return roomRepository.findAllByLocationId(locationId, pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Room getRoomById(UUID roomId) throws RoomNotFoundException {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));
    }

    public Room updateRoom(UUID roomId, Room room, String eTagReceived) throws RoomNotFoundException, OptLockException {
        Room roomToUpdate = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(ExceptionMessages.ROOM_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTagReceived, String.valueOf(roomToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        roomToUpdate.setName(room.getName());
        roomToUpdate.setMaxCapacity(room.getMaxCapacity());
        return roomRepository.saveAndFlush(roomToUpdate);
    }
}
