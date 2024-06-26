package pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.get;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Sort;

public record GetAccountPageDTO(

        Integer page,

        int elementPerPage,

        String direction,

        String key,

        String phrase
) {

    public Sort buildSort() {
        if (direction.equals("asc")) {
            return Sort.by(key).ascending();
        }
        return Sort.by(key).descending();
    }

}
