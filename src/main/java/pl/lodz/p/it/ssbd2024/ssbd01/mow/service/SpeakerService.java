package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.SpeakerRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpeakerService {

    private final SpeakerRepository speakerRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void getAllSpeakers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void createSpeaker() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public void updateSpeaker(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
