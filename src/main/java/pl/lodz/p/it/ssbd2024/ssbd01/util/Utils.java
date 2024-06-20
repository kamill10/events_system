package pl.lodz.p.it.ssbd2024.ssbd01.util;

import org.springframework.security.access.prepost.PreAuthorize;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Session;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mow.Ticket;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.WrongRoleToAccountException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.AlreadySignUpException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mow.SessionNotActiveException;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.AccountRoleEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.util.List;

public class Utils {

    public static LocalDateTime calculateExpirationDate(int expirationHours) {
        return LocalDateTime.now().plusHours(expirationHours);
    }

    public static void canAddManagerOrAdminRole(Account account) throws AppException {
        List<Role> accountRoles = account.getRoles();
        if (accountRoles.contains(new Role(AccountRoleEnum.ROLE_PARTICIPANT))) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
    }

    public static void canAddParticipantRole(Account account) throws AppException {
        List<Role> accountRoles = account.getRoles();
        if (!accountRoles.isEmpty()) {
            throw new WrongRoleToAccountException(ExceptionMessages.PARTICIPANT_CANNOT_HAVE_OTHER_ROLES);
        }
    }

    public static boolean  isSessionActive(Session session) {
        return session.getIsActive() && !session.getEndTime().isBefore(LocalDateTime.now());
    }

}
