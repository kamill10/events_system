package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomDTOConverter {

    public GetRoomDTO toRoomDto(Room room) {
        return new GetRoomDTO(
                room.getId(),
                room.getName(),
                room.getMaxCapacity()
        );
    }

    public Room toRoom(UpdateRoomDTO room) {
        return new Room(
                room.name(),
                room.maxCapacity()
        );
    }

    public List<GetRoomDTO> toRoomDto(List<Room> rooms) {
        return rooms.stream().map(this::toRoomDto).collect(Collectors.toList());
    }

}
