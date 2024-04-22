package pl.lodz.p.it.ssbd2024.ssbd01.auth.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import java.util.UUID;

public interface AccountAuthRepository extends JpaRepository<Account, UUID> {

    @Transactional
    Account findByUsername(String username);
}
