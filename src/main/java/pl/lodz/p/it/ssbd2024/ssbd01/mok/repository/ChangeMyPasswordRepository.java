package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ChangeMyPassword;

@Transactional(propagation = Propagation.MANDATORY)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface ChangeMyPasswordRepository extends GenericChangeCredentialTokenRepository<ChangeMyPassword> {

}
