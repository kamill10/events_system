package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.ChangeEmail;

@Transactional(propagation = Propagation.MANDATORY)
public interface ChangeEmailRepository extends GenericChangeCredentialTokenRepository<ChangeEmail> {

}
