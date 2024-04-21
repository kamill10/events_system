package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import java.util.Optional;
import java.util.UUID;


public interface AccountMokRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);
}
