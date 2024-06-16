package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import pl.lodz.p.it.ssbd2024.ssbd01.dto.mok.get.GetAccountHistoryDetailedDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSpeakerDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.get.GetSpeakerHistoryDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountHistory;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Speaker;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.SpeakerHistory;

import java.util.List;


public class SpeakerDTOConverter {

    public static GetSpeakerDTO convertToDTO(Speaker speaker) {
        return new GetSpeakerDTO(
                speaker.getId(),
                speaker.getFirstName(),
                speaker.getLastName());
    }

    public static GetSpeakerHistoryDTO convertToHistoryDTO(SpeakerHistory speakerHistory) {

        String createdBy = speakerHistory.getCreatedBy() != null ? speakerHistory.getCreatedBy().getUsername() : null;
        String updatedBy = speakerHistory.getUpdatedBy() != null ? speakerHistory.getUpdatedBy().getUsername() : null;

        return new GetSpeakerHistoryDTO(
                speakerHistory.getId(),
                speakerHistory.getFirstName(),
                speakerHistory.getLastName(),
                speakerHistory.getCreatedAt(),
                speakerHistory.getUpdatedAt(),
                createdBy,
                updatedBy,
                speakerHistory.getActionType());
    }

    public static List<GetSpeakerHistoryDTO> convertToHistoryDTOList(List<SpeakerHistory> speakerHistories) {
        return speakerHistories.stream()
                .map(SpeakerDTOConverter::convertToHistoryDTO)
                .toList();
    }

}
