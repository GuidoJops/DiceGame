package DiceGame.controllers;

import DiceGame.model.dto.AuthResponse;
import DiceGame.model.dto.AuthRequest;
import DiceGame.model.services.IAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequest authRequest) {
        if (authService.registerUser(authRequest) == null){
            log.error("Fallo en el registro de Usuario");
            return new ResponseEntity<>("Nombre de Usuario ya existe", HttpStatus.CONFLICT);
        }
        log.info("Usuario Registrado");
        return new ResponseEntity<>("Usuario registrado con éxito!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.loginUser(authRequest);
        if (authResponse == null){
            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

}
