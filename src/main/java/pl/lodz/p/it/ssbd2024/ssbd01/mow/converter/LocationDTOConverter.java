package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;



public class LocationDTOConverter {

    public static GetLocationDTO toLocationDto(Location location) {
        return new GetLocationDTO(
                location.getId(),
                location.getName(),
                location.getStreet(),
                location.getBuildingNumber(),
                location.getPostalCode(),
                location.getCity(),
                location.getCountry()
        );
    }


    public static Page<GetLocationDTO> locationDTOPage(Page<Location> locationPage) {
        return locationPage.map(LocationDTOConverter::toLocationDto);
    }


    public static Location toLocation(CreateLocationDTO createLocationDTO) {
        return new Location(
                createLocationDTO.name(),
                createLocationDTO.street(),
                createLocationDTO.buildingNumber(),
                createLocationDTO.postalCode(),
                createLocationDTO.city(),
                createLocationDTO.country()
        );
    }

    public static Location toLocation(UpdateLocationDTO updateLocationDTO) {
        return new Location(
                updateLocationDTO.name(),
                updateLocationDTO.street(),
                updateLocationDTO.buildingNumber(),
                updateLocationDTO.postalCode(),
                updateLocationDTO.city(),
                updateLocationDTO.country()
        );
    }

    public static GetLocationDTO toGetLocationDTO(Location location) {
        return new GetLocationDTO(
                location.getId(),
                location.getName(),
                location.getStreet(),
                location.getBuildingNumber(),
                location.getPostalCode(),
                location.getCity(),
                location.getCountry()
        );
    }


}
