package pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get;

import java.util.UUID;

public record GetSpeakerDTO(
        UUID id,
        String firstName,
        String lastName
) {
}
