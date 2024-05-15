package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractCredentialChange;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@NoRepositoryBean
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface GenericChangeCredentialTokenRepository<T extends AbstractCredentialChange> extends JpaRepository<T, UUID> {

    @PreAuthorize("permitAll()")
    Optional<T> findByToken(String token);

    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    void deleteAllByExpirationDateBefore(LocalDateTime expirationDate);

    @PreAuthorize("permitAll()")
    void deleteByToken(String token);

    @PreAuthorize("permitAll()")
    @Override
    <S extends T> S saveAndFlush(S entity);
}
