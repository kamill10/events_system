package pl.lodz.p.it.ssbd2024.ssbd01.dto.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

public record GetAccountDTO(
    UUID id,
    String username,
    String email,
    Integer gender,
    String firstName,
    String lastName
){}
