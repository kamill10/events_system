package pl.lodz.p.it.ssbd2024.ssbd01.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.ssbd2024.ssbd01.request.mauth.LoginRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.request.mauth.RegisterUserRequest;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.services.AuthenticationService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/user")
    public ResponseEntity<String> registerUser(
            @RequestBody RegisterUserRequest request
    ) {
        return ResponseEntity.ok(service.registerUser(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.authenticate(request));
    }
}
