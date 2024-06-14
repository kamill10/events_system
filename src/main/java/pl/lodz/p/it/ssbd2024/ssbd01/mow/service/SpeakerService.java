package pl.lodz.p.it.ssbd2024.ssbd01.mow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Speaker;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.OptLockException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.SpeakerNotFoundException;
import pl.lodz.p.it.ssbd2024.ssbd01.mow.repository.SpeakerRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ETagBuilder;
import pl.lodz.p.it.ssbd2024.ssbd01.util.PageUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpeakerService {

    private final SpeakerRepository speakerRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Page<Speaker> getAllSpeakers(PageUtils pageUtils) {
        return speakerRepository.findAll(pageUtils.buildPageable());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Speaker createSpeaker(Speaker speaker) {
        return speakerRepository.saveAndFlush(speaker);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Speaker updateSpeaker(UUID id,Speaker speaker,String eTagReceived) throws AppException {
        Speaker speakerToUpdate = speakerRepository.findById(id)
                .orElseThrow(() -> new SpeakerNotFoundException(ExceptionMessages.SPEAKER_NOT_FOUND));
        if (!ETagBuilder.isETagValid(eTagReceived, String.valueOf(speakerToUpdate.getVersion()))) {
            throw new OptLockException(ExceptionMessages.OPTIMISTIC_LOCK_EXCEPTION);
        }
        speakerToUpdate.setFirstName(speaker.getFirstName());
        speakerToUpdate.setLastName(speaker.getLastName());
        return speakerRepository.saveAndFlush(speakerToUpdate);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Speaker getSpeaker(UUID id) throws AppException {
        return speakerRepository.findById(id)
                .orElseThrow(() -> new SpeakerNotFoundException(ExceptionMessages.SPEAKER_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public List<Speaker> searchSpeakers(String firstName, String lastName) {
        return speakerRepository.findAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(firstName, lastName);
    }

}
