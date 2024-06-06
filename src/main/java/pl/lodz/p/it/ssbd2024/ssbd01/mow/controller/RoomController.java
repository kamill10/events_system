package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.RoomNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.RoomDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.RoomService;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{locationId}")
    public ResponseEntity<List<GetRoomDTO>> getAllRooms(@PathVariable UUID locationId) {
        List<GetRoomDTO> rooms = RoomDTOConverter.toRoomDto(roomService.getAllLocationRooms(locationId));
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<GetRoomDTO> getRoomById(@PathVariable UUID roomId) throws RoomNotFoundException {
        Room room = roomService.getRoomById(roomId);
        String eTag = ETagBuilder.buildETag(room.getVersion().toString());
        return ResponseEntity.ok()
                .header("ETag", eTag)
                .body(RoomDTOConverter.toRoomDto(room));
    }

    @PatchMapping("/room/{roomId}")
    public ResponseEntity<GetRoomDTO> updateRoom(@RequestHeader("If-Match") String eTagReceived, @PathVariable UUID roomId, @RequestBody
    UpdateRoomDTO roomDTO) throws RoomNotFoundException, OptLockException {
        return ResponseEntity.ok()
                .body(RoomDTOConverter.toRoomDto(roomService
                        .updateRoom(roomId, RoomDTOConverter.toRoom(roomDTO), eTagReceived)));
    }

}
