package pl.lodz.p.it.ssbd2024.ssbd01.mow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.converter.RoomDTOConverter;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.service.RoomService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomDTOConverter roomDTOConverter;

    @GetMapping("/{locationId}")
    public ResponseEntity<List<GetRoomDTO>> getAllRooms(@PathVariable UUID locationId) {
        List<GetRoomDTO> rooms = roomDTOConverter.toRoomDto(roomService.getAllLocationRooms(locationId));
        return ResponseEntity.ok(rooms);
    }

}
