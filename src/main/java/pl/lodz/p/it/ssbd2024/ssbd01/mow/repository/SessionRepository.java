package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;

import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface SessionRepository extends JpaRepository<Session, UUID> {
}
