package pl.lodz.p.it.ssbd2024.ssbd01.mok.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.*;

@Data
public class CreateUserRequest {
    private  String username;
    private  String password;
    private  String email;
    private  Integer gender;
    private  String firstName;
    private  String lastName;
}
