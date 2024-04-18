package pl.lodz.p.it.ssbd2024.ssbd01.dto.update;

public record UpdateAccountDTO(
        String email,
        Integer gender,
        String password
) {
}
