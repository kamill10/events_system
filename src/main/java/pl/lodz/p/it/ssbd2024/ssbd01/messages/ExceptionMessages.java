package pl.lodz.p.it.ssbd2024.ssbd01.messages;

public class ExceptionMessages {
    public static final String ROLE_NOT_FOUND = "Role with given name does not exist.";
    public static final String ROLE_ALREADY_ASSIGNED = "Role  is already assigned.";
    public static final String PARTICIPANT_CANNOT_HAVE_OTHER_ROLES = "Participant can't have other roles.";
    public static final String ACCOUNT_NOT_HAVE_THIS_ROLE = "This account does not have this  role";
    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists.";
    public static final String TOKEN_EXPIRED = "Token has expired.";
    public static final String TOKEN_NOT_FOUND = "Token not found.";
    public static final String PASS_TOKEN_USED = "Token has been used.";
    public static final String CONFIRMATION_TOKEN_EXPIRED = "Confirmation token has expired.";
    public static final String CONFIRMATION_TOKEN_NOT_FOUND = "Confirmation token not found.";
    public static final String THIS_PASSWORD_ALREADY_WAS_SET_IN_HISTORY = "This password already was set in history.";
    public static final String OPTIMISTIC_LOCK_EXCEPTION = "Optimistic lock exception.";
    public static final String WRONG_OLD_PASSWORD = "Old password is not correct";
    public static final String EMAIL_RESET_TOKEN_NOT_FOUND = "Email reset token not found.";


}
