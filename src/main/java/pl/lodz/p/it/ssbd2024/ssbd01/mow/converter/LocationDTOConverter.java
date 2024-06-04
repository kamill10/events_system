//package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;
//
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Component;
//import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetLocationDTO;
//import pl.lodz.p.it.ssbd2024.ssbd01.dto.get.GetRoomDTO;
//import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;
//import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;
//
//
//@Component
//public class LocationDTOConverter {
//
//    public Page<GetLocationDTO> locationDTOPage(Page<Location> locationPage) {
//        return locationPage.map(this::toLocationDTO);
//    }
//
//    public GetLocationDTO toLocationDTO(Location location) {
//        return new GetLocationDTO(
//                location.getId(),
//                location.getName(),
//                location.getRooms().stream().map(this::toRoomDTO).toList()
//        );
//    }
//
//    public GetRoomDTO toRoomDTO(Room room) {
//        return new GetRoomDTO(
//                room.getId(),
//                room.getName(),
//                room.getMaxCapacity()
//        );
//    }
//
//}
