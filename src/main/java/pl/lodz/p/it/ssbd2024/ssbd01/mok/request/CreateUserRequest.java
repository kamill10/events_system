package pl.lodz.p.it.ssbd2024.ssbd01.mok.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private  String username;
    private  String password;
    private  String email;
    private  Integer gender;
    private  String firstName;
    private  String lastName;
}
