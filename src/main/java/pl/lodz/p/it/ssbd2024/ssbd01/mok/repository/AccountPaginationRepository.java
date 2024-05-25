package pl.lodz.p.it.ssbd2024.ssbd01.mok.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import java.util.UUID;

@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AccountPaginationRepository extends PagingAndSortingRepository<Account, UUID> {

}
