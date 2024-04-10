package pl.lodz.p.it.ssbd2024.ssbd01.entities.mok;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.util.AbstractEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "account")
//@SecondaryTable(name = "personal_data", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id"))
public class Account extends AbstractEntity implements UserDetails {

    @Setter(AccessLevel.NONE)
    @Column(unique = true, updatable = false, nullable = false, length = 32)
    @Size(min = 3, max = 32)
    @NotBlank
    private String username;

    @ToString.Exclude
    @Column(nullable = false, length = 72)
    @Size(min = 8, max = 72)
    @NotNull
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

    //    @Column(name = "email", table = "personal_data")
    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    //    @Column(table = "personal_data")
    @Column(nullable = false)
    @NotNull
    private Integer gender;

    //    @Column(table = "personal_data")
    @Column(nullable = false, length = 32)
    @NotBlank
    @Size(max = 32)
    private String firstName;

    //    @Column(table = "personal_data")
    @Column(nullable = false, length = 64)
    @NotBlank
    @Size(max = 64)
    private String lastName;


    public Account(String username, String password, String email, Integer gender, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = true;
        this.verified = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) role::getName)
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
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
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return username != null && username.equals(account.getUsername());
    }

    @Override
    public final int hashCode() {
        if (username != null) return username.hashCode();
        return super.hashCode();
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role){
        roles.remove(role);
    }

}
