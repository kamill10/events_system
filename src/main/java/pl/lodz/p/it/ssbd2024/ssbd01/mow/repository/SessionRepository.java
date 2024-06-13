package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_PARTICIPANT')")
public interface SessionRepository extends JpaRepository<Session, UUID> {

    @PreAuthorize("permitAll()")
    List<Session> getByEventId(UUID eventId);

    @PreAuthorize("permitAll()")
    @Override
    Optional<Session> findById(UUID id);

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Query("SELECT s FROM Session s WHERE s.event.id = :id AND (s.startTime < :startTime OR s.endTime > :endTime)")
    List<Session> findSessionsOutsideRange(UUID id, LocalDateTime startTime, LocalDateTime endTime);

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Query("SELECT s FROM Session s WHERE s.room.id = :roomId AND (s.startTime > :startTime AND s.endTime < :endTime)")
    List<Session> findSessionsInsideRangeAtRoom(UUID roomId, LocalDateTime startTime, LocalDateTime endTime);

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Query("SELECT s FROM Session s WHERE s.speaker.id = :speakerId AND (s.startTime > :startTime AND s.endTime < :endTime)")
    List<Session> findSpeakerSessionsInRange(UUID speakerId, LocalDateTime startTime, LocalDateTime endTime);
}
