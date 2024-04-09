package pl.lodz.p.it.ssbd2024.ssbd01.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import java.util.UUID;

public interface AccountAuthRepository extends JpaRepository<Account, UUID> {
    Account findByUsername(String username);
}
