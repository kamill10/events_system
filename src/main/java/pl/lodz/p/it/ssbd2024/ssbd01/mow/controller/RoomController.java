package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.LocationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.RoomNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.RoomDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.RoomService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{locationId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Page<GetRoomDTO>> getAllRooms(
            @PathVariable UUID locationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "id") String key
    ) {
        PageUtils pageUtils = new PageUtils(page, size, direction, key);
        Page<Room> rooms = roomService.getAllLocationRooms(locationId, pageUtils);
        return ResponseEntity.status(HttpStatus.OK).body(RoomDTOConverter.roomDTOPage(rooms));
    }

    @GetMapping("/{locationId}/all")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<GetRoomDTO>> getAllRooms(
            @PathVariable UUID locationId
    ) {
        var allRooms = roomService.getAllLocationRooms(locationId)
                .stream()
                .map(RoomDTOConverter::toRoomDto)
                .toList();

        return ResponseEntity.ok()
                .body(allRooms);
    }

    @GetMapping("/deleted/{locationId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<GetRoomDTO>> getDeletedRooms(@PathVariable UUID locationId) {
        var rooms = roomService.getAllDeletedRooms(locationId)
                .stream()
                .map(RoomDTOConverter::toRoomDto)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(rooms);
    }

    @GetMapping("/room/deleted/{roomId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<GetRoomDTO> getDeletedRoomById(@PathVariable UUID roomId) throws AppException {
        Room room = roomService.getDeletedRoomById(roomId);
        String eTag = ETagBuilder.buildETag(room.getVersion().toString());

        return ResponseEntity.ok()
                .header("ETag", eTag)
                .body(RoomDTOConverter.toRoomDto(room));
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<GetRoomDTO> getRoomById(@PathVariable UUID roomId) throws AppException {
        Room room = roomService.getRoomById(roomId);
        String eTag = ETagBuilder.buildETag(room.getVersion().toString());

        return ResponseEntity.ok()
                .header("ETag", eTag)
                .body(RoomDTOConverter.toRoomDto(room));
    }

    @PostMapping("/room")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<UUID> createRoom(@RequestBody @Valid CreateRoomDTO createRoomDTO) throws AppException {
        Room room = RoomDTOConverter.toRoom(createRoomDTO);
        var roomCreated = roomService.createRoom(room, createRoomDTO.locationId());
        return ResponseEntity.ok()
                .body(roomCreated.getId());
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@RequestHeader("If-Match") String eTagReceived, @PathVariable UUID roomId)
            throws AppException {
        roomService.deleteRoom(roomId, eTagReceived);

        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/room/activate/{roomId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> activateRoom(@RequestHeader("If-Match") String eTagReceived, @PathVariable UUID roomId)
            throws AppException {
        roomService.activateRoom(roomId, eTagReceived);

        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<GetRoomDTO> updateRoom(@RequestHeader("If-Match") String eTagReceived, @PathVariable UUID roomId, @RequestBody
    UpdateRoomDTO roomDTO) throws AppException {
        return ResponseEntity.ok()
                .body(RoomDTOConverter.toRoomDto(roomService
                        .updateRoom(roomId, RoomDTOConverter.toRoom(roomDTO), eTagReceived)));
    }
}