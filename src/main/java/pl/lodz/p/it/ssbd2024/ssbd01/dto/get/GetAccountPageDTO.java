package pl.lodz.p.it.ssbd2024.ssbd01.dto.get;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Sort;

public record GetAccountPageDTO(
        @NotNull
        @PositiveOrZero
        Integer page,
        @NotNull
        @Positive
        int elementPerPage,
        @Pattern(regexp = "asc|desc")
        String direction,
        @NotNull
        String key,
        @NotNull
        String phrase
) {

    public Sort buildSort() {
        if (direction.equals("asc")) {
            return Sort.by(key).ascending();
        }
        return Sort.by(key).descending();
    }

}
