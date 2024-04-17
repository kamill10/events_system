package pl.lodz.p.it.ssbd2024.ssbd01.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.services.AuthenticationService;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.LoginDTO;
import pl.lodz.p.it.ssbd2024.ssbd01.dto.create.CreateAccountDTO;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/user")
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
