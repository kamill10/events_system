package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Location;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;

import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_MANAGER')")
public interface LocationRepository extends JpaRepository<Location, UUID> {

    @Override
    @PreAuthorize("permitAll()")
    Optional<Location> findById(UUID id);

    Page<Location> findAllByIsActiveTrue(Pageable pageable);

    Page<Location> findAllByIsActiveFalse(Pageable pageable);

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Override
    Location saveAndFlush(Location ticket);

}
