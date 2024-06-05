package pl.lodz.p.it.ssbd2024.ssbd01.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageUtils(Integer page, int elementPerPage, String direction, String key) {

    private Sort buildSort() {
        if (direction.equals("asc")) {
            return Sort.by(key).ascending();
        }
        return Sort.by(key).descending();
    }

    public Pageable buildPageable() {
        return PageRequest.of(page, elementPerPage, buildSort());
    }
}
