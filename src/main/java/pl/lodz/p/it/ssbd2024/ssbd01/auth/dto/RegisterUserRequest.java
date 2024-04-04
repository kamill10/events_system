package pl.lodz.p.it.ssbd2024.ssbd01.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    private  String username;
    private  String password;
    private  String email;
    private  Integer gender;
    private  String firstName;
    private  String lastName;

    public RegisterUserRequest(String username, String password, String email, Integer gender, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public RegisterUserRequest(){};
}
