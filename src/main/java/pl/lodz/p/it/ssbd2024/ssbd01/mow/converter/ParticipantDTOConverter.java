package pl.lodz.p.it.ssbd2024.ssbd01.mow.converter;

import com.atomikos.icatch.Participant;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.mow.create.GetParticipantDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

public class ParticipantDTOConverter {
    public static GetParticipantDTO getParticipantDTO(Account participant) {
        return new GetParticipantDTO(participant.getFirstName(), participant.getLastName(), participant.getEmail(), participant.getUsername());
    }
}
