package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
}
