package pl.lodz.p.it.ssbd2024.ssbd01.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import java.util.UUID;

public interface AccountAuthRepository extends JpaRepository<Account, UUID> {

    @Transactional
    Account findByUsername(String username);
}
