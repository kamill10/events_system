package pl.lodz.p.it.ssbd2024.ssbd01.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private UUID id;
    private String username;
    private String email;
    private Integer gender;
    private String firstName;
    private String lastName;


}
