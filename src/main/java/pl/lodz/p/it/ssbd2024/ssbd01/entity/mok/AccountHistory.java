package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.ActionTypeEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.AbstractEntity;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "account_history")
@SecondaryTable(name = "personal_data_history", pkJoinColumns = @PrimaryKeyJoinColumn(name = "account_history_id"))
public class AccountHistory extends AbstractEntity {

    @Setter(AccessLevel.NONE)
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;

    @Setter(AccessLevel.NONE)
    @Column(updatable = false, nullable = false, length = 32)
    @Size(min = 3, max = 32, message = ExceptionMessages.INCORRECT_USERNAME)
    @NotBlank(message = ExceptionMessages.INCORRECT_USERNAME)
    @Pattern(regexp = "^(?!anonymous$)[a-zA-Z0-9]{3,32}$", message = ExceptionMessages.INCORRECT_USERNAME)
    private String username;

    @ToString.Exclude
    @Column(nullable = false, length = 72)
    @Size(min = 8, max = 72, message = ExceptionMessages.INCORRECT_PASSWORD)
    @NotNull(message = ExceptionMessages.INCORRECT_PASSWORD)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();

    @PastOrPresent
    private LocalDateTime lastSuccessfulLogin;

    @PastOrPresent
    private LocalDateTime lastFailedLogin;

    @Column(nullable = false)
    @NotNull
    private Boolean active;

    @Column(nullable = false)
    @NotNull
    private Boolean verified;

    @Column(nullable = false)
    @NotNull
    private Boolean nonLocked;

    @Column(nullable = false)
    @NotNull
    private Integer failedLoginAttempts;

    @Column(length = 39)
    @Size(max = 39)
    private String lastFailedLoginIp;

    @Column(length = 39)
    @Size(max = 39)
    private String lastSuccessfulLoginIp;

    @FutureOrPresent
    private LocalDateTime lockedUntil;

    @Column(table = "personal_data_history", nullable = false)
    @NotBlank(message = ExceptionMessages.INCORRECT_EMAIL)
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    private String email;

    @Column(table = "personal_data_history", nullable = false)
    @NotNull(message = ExceptionMessages.INCORRECT_GENDER)
    private Integer gender;

    @Column(table = "personal_data_history", nullable = false, length = 32)
    @NotBlank(message = ExceptionMessages.INCORRECT_FIRST_NAME)
    @Size(min = 2, max = 32, message = ExceptionMessages.INCORRECT_FIRST_NAME)
    private String firstName;

    @Column(table = "personal_data_history", nullable = false, length = 64)
    @NotBlank(message = ExceptionMessages.INCORRECT_LAST_NAME)
    @Size(min = 2, max = 64, message = ExceptionMessages.INCORRECT_LAST_NAME)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageEnum language;

    @ManyToOne
    private AccountTheme accountTheme;

    @ManyToOne
    private AccountTimeZone accountTimeZone;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false)
    private Account createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "action_type", nullable = false)
    private ActionTypeEnum actionType;

    public AccountHistory(Account account) {
        this.account = account;
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.roles = List.of(Arrays.copyOf(account.getRoles().toArray(), account.getRoles().size(), Role[].class));
        this.lastSuccessfulLogin = account.getLastSuccessfulLogin();
        this.lastFailedLogin = account.getLastFailedLogin();
        this.active = account.getActive();
        this.verified = account.getVerified();
        this.nonLocked = account.getNonLocked();
        this.failedLoginAttempts = account.getFailedLoginAttempts();
        this.lastFailedLoginIp = account.getLastFailedLoginIp();
        this.lastSuccessfulLoginIp = account.getLastSuccessfulLoginIp();
        this.lockedUntil = account.getLockedUntil();
        this.email = account.getEmail();
        this.gender = account.getGender();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.language = account.getLanguage();
        this.accountTheme = account.getAccountTheme();
        this.accountTimeZone = account.getAccountTimeZone();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
        this.createdBy = account.getCreatedBy();
        this.updatedBy = account.getUpdatedBy();
        this.actionType = account.getActionType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountHistory accountHistory)) {
            return false;
        }
        return username != null && username.equals(accountHistory.getUsername());
    }

    @Override
    public final int hashCode() {
        if (username != null) {
            return username.hashCode();
        }
        return super.hashCode();
    }

}
