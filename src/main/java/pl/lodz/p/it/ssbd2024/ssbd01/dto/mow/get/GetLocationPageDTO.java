package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import org.springframework.data.domain.Sort;

public record GetLocationPageDTO(

        Integer page,

        int elementPerPage,

        String direction,

        String key
) {

    public Sort buildSort() {
        if (direction.equals("asc")) {
            return Sort.by(key).ascending();
        }
        return Sort.by(key).descending();
    }

}
