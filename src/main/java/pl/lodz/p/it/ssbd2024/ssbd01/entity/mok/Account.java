package pl.lodz.p.it.ssbd2024.ssbd01.entity.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.it.ssbd2024.ssbd01.util._enum.LanguageEnum;
import pl.lodz.p.it.ssbd2024.ssbd01.util.ControlledEntity;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "account")
@SecondaryTable(name = "personal_data", pkJoinColumns = @PrimaryKeyJoinColumn(name = "account_id"))
public class Account extends ControlledEntity implements UserDetails {

    @Setter(AccessLevel.NONE)
    @Column(unique = true, updatable = false, nullable = false, length = 32)
    @Size(min = 3, max = 32, message = ExceptionMessages.INCORRECT_USERNAME)
    @NotBlank(message = ExceptionMessages.INCORRECT_USERNAME)
    @Pattern(regexp = "^(?!system$)(?!anonymous$)[a-zA-Z0-9]{3,32}$", message = ExceptionMessages.INCORRECT_USERNAME)
    private String username;

    @ToString.Exclude
    @Column(nullable = false, length = 72)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,72}$", message = ExceptionMessages.INCORRECT_PASSWORD)
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

    @Column(table = "personal_data", nullable = false, unique = true)
    @NotBlank(message = ExceptionMessages.INCORRECT_EMAIL)
    @Email(message = ExceptionMessages.INCORRECT_EMAIL)
    private String email;

    @Column(table = "personal_data", nullable = false)
    @NotNull(message = ExceptionMessages.INCORRECT_GENDER)
    private Integer gender;

    @Column(table = "personal_data", nullable = false, length = 32)
    @NotBlank(message = ExceptionMessages.INCORRECT_FIRST_NAME)
    @Size(min = 2, max = 32, message = ExceptionMessages.INCORRECT_FIRST_NAME)
    private String firstName;

    @Column(table = "personal_data", nullable = false, length = 64)
    @NotBlank(message = ExceptionMessages.INCORRECT_LAST_NAME)
    @Size(min = 2, max = 64, message = ExceptionMessages.INCORRECT_LAST_NAME)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageEnum language;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountTheme accountTheme;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountTimeZone accountTimeZone;


    public Account(String username, String password, String email, Integer gender, String firstName, String lastName, LanguageEnum language) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = true;
        this.verified = false;
        this.nonLocked = true;
        this.failedLoginAttempts = 0;
        this.language = language;
    }

    public Account(String firstName, String lastName, Integer gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return verified;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account account)) {
            return false;
        }
        return username != null && username.equals(account.getUsername());
    }

    @Override
    public final int hashCode() {
        if (username != null) {
            return username.hashCode();
        }
        return super.hashCode();
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

}
