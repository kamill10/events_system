package pl.lodz.p.it.ssbd2024.ssbd01.repositories.mok;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.entity.Account;

import java.util.UUID;


@Repository
public interface UserRepository  extends JpaRepository<Account, UUID> {
    Account findByUsername(String username);
}
