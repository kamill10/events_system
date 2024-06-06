package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.CreateLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.update.UpdateLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;


@Component
public class LocationDTOConverter {

    public GetLocationDTO toLocationDto(Location location) {
        return new GetLocationDTO(
                location.getId(),
                location.getName(),
                location.getCity(),
                location.getCountry(),
                location.getStreet(),
                location.getBuildingNumber(),
                location.getPostalCode()
        );
    }


    public Page<GetLocationDTO> locationDTOPage(Page<Location> locationPage) {
        return locationPage.map(this::toLocationDto);
    }


    public Location toLocation(CreateLocationDTO createLocationDTO) {
        return new Location(
                createLocationDTO.name(),
                createLocationDTO.city(),
                createLocationDTO.country(),
                createLocationDTO.street(),
                createLocationDTO.buildingNumber(),
                createLocationDTO.postalCode()
        );
    }

    public Location toLocation(UpdateLocationDTO updateLocationDTO) {
        return new Location(
                updateLocationDTO.name(),
                updateLocationDTO.city(),
                updateLocationDTO.country(),
                updateLocationDTO.street(),
                updateLocationDTO.buildingNumber(),
                updateLocationDTO.postalCode()
        );
    }

    public GetLocationDTO toGetLocationDTO(Location location) {
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
