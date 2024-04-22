package pl.lodz.p.it.ssbd2024.ssbd01.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.service.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody CreateAccountDTO request
    ) {
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody LoginDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request));
    }
}
