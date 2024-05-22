package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity._enum.TimeZoneEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountTimeZone;

import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AccountTimeZoneRepository  extends JpaRepository<AccountTimeZone, UUID> {

    @PreAuthorize("permitAll()")
    Optional<AccountTimeZone> findByTimeZoneEnum(TimeZoneEnum timeZoneEnum);
}
