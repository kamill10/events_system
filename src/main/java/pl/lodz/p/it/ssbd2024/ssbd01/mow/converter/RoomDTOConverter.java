package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateRoomDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetRoomDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;

import java.util.List;
import java.util.stream.Collectors;

public class RoomDTOConverter {

    public static GetRoomDTO toRoomDto(Room room) {
        return new GetRoomDTO(
                room.getId(),
                room.getName(),
                room.getMaxCapacity()
        );
    }

    public static List<GetRoomDTO> toRoomDto(List<Room> rooms) {
        return rooms.stream().map(RoomDTOConverter::toRoomDto).collect(Collectors.toList());
    }

    public static  Room toRoom(UpdateRoomDTO room) {
        return new Room(
                room.name(),
                room.maxCapacity()
        );
    }

    public static GetRoomDetailedDTO toRoomDetailedDto(Room room) {
        return new GetRoomDetailedDTO(
                room.getId(),
                room.getName(),
                room.getMaxCapacity(),
                LocationDTOConverter.toLocationDto(room.getLocation())
        );
    }

}