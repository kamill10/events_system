package pl.lodz.p.it.ssbd2024.ssbd01.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {
    private  String username;
    private  String password;
    private  String email;
    private  Integer gender;
    private  String firstName;
    private  String lastName;


}
