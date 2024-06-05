package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Speaker;

public class SpeakerDTOConverter {
    public static GetSpeakerDTO convertToDTO(Speaker speaker) {
        return new GetSpeakerDTO(
                speaker.getId(),
                speaker.getFirstName(),
                speaker.getLastName());
    }
}
