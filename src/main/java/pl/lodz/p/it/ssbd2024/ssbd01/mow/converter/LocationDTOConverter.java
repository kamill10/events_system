package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetLocationDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;


@Component
public class LocationDTOConverter {

    public GetLocationDTO toLocationDto(Location location) {
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


    public Page<GetLocationDTO> locationDTOPage(Page<Location> locationPage) {
        return locationPage.map(this::toLocationDto);
    }

}
