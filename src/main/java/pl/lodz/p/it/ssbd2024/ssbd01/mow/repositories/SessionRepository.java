package pl.lodz.p.it.ssbd2024.ssbd01.mow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.Session;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
}
