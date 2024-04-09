package pl.lodz.p.it.ssbd2024.ssbd01.mow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.Event;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}