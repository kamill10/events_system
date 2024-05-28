package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.AccountTheme;

import java.util.Optional;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface ThemeRepository extends JpaRepository<AccountTheme, UUID> {

    @PreAuthorize("permitAll()")
    Optional<AccountTheme> findByTheme(String theme);
}
