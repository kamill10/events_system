package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;

import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_MANAGER')")
public interface RoomRepository extends JpaRepository<Room, UUID> {

    Page<Room> findAllByLocationIdAndIsActiveTrue(UUID locationId, Pageable pageUtils);

    Optional<Room> findByIdAndIsActiveTrue(UUID id);
}
