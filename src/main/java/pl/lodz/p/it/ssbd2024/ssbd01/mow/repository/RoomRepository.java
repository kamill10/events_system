package pl.lodz.p.it.ssbd2024.ssbd01.mow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Room;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
}
