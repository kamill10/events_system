package pl.lodz.p.it.ssbd2024.ssbd01.util.messages;

import org.springframework.http.HttpStatusCode;

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
    public static final String OPTIMISTIC_LOCK_EXCEPTION = "OPTLCKE";
    public static final String WRONG_OLD_PASSWORD = "Old password is not correct";
    public static final String EMAIL_RESET_TOKEN_NOT_FOUND = "Email reset token not found.";
    public static final String INCORRECT_EMAIL = "Incorrect email.";
    public static final String INCORRECT_PASSWORD = "Incorrect password.";
    public static final String INCORRECT_USERNAME = "Incorrect username.";
    public static final String INCORRECT_GENDER = "Incorrect gender";
    public static final String INCORRECT_FIRST_NAME = "Incorrect first name";
    public static final String INCORRECT_LAST_NAME = "Incorrect last name";
    public static final String INCORRECT_LANGUAGE = "Incorrect language";
    public static final String ACCOUNT_THEME_NOT_FOUND = "Theme not found";
    public static final String UNLOCK_TOKEN_NOT_FOUND = "Unlock token not found.";
    public static final String TIME_ZONE_NOT_FOUND = "Time zone not found";
    public static final String ACCOUNT_LOCKED = "Account is locked.";

    public static final String SOMETHING_WENT_WRONG = "Something went wrong.";
    public static final String ACCOUNT_NOT_VERIFIED = "Account is not verified.";
    public static final String RESOURCE_NOT_FOUND = "Resource not found.";
    public static final String BAD_REQUEST = "Bad request.";
    public static final String UNPROCESSABLE_ENTITY = "Unprocessable entity.";
    public static final String FORBIDDEN = "Forbidden.";
    public static final String PRECONDITION_FAILED = "Precondition failed.";
}
