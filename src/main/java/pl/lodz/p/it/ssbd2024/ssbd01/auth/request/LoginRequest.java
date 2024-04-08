package pl.lodz.p.it.ssbd2024.ssbd01.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class LoginRequest {
    private String username;
    private String password;



}
