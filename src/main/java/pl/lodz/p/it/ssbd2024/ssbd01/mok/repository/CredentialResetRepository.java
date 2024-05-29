package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.CredentialReset;

@Transactional(propagation = Propagation.MANDATORY)
public interface CredentialResetRepository extends GenericChangeCredentialTokenRepository<CredentialReset> {
}
