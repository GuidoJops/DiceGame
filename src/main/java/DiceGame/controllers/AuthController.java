package DiceGame.controllers;

import DiceGame.model.dto.LoginResponse;
import DiceGame.model.dto.AuthLoginRequest;
import DiceGame.services.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthLoginRequest authLoginRequest) {
        authService.registerUser(authLoginRequest);
        return new ResponseEntity<>("Successfully registered", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody AuthLoginRequest authLoginRequest) {
        LoginResponse loginResponse = authService.loginUser(authLoginRequest);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

}
