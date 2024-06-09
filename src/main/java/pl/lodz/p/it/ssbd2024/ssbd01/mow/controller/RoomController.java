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
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.LocationNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.RoomNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.RoomDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.RoomService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;

import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{locationId}")
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

    @GetMapping("/room/{roomId}")
    public ResponseEntity<GetRoomDTO> getRoomById(@PathVariable UUID roomId) throws RoomNotFoundException {
        Room room = roomService.getRoomById(roomId);
        String eTag = ETagBuilder.buildETag(room.getVersion().toString());
        return ResponseEntity.ok()
                .header("ETag", eTag)
                .body(RoomDTOConverter.toRoomDto(room));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody @Valid CreateRoomDTO createRoomDTO) throws LocationNotFoundException {
        Room room = RoomDTOConverter.toRoom(createRoomDTO);
        roomService.createRoom(room, createRoomDTO.locationId());
        return ResponseEntity.ok()
                .build();
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@RequestHeader("If-Match") String eTagReceived, @PathVariable UUID roomId)
            throws OptLockException, RoomNotFoundException {
        roomService.deleteRoom(roomId, eTagReceived);

        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/room/{roomId}")
    public ResponseEntity<GetRoomDTO> updateRoom(@RequestHeader("If-Match") String eTagReceived, @PathVariable UUID roomId, @RequestBody
    UpdateRoomDTO roomDTO) throws RoomNotFoundException, OptLockException {
        return ResponseEntity.ok()
                .body(RoomDTOConverter.toRoomDto(roomService
                        .updateRoom(roomId, RoomDTOConverter.toRoom(roomDTO), eTagReceived)));
    }
}