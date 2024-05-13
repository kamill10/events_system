package pl.lodz.p.it.ssbd2024.ssbd01.dto;

import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

public record CredentialUpdateArgumentDTO(String newCredential,
                                          Account account) {
}
