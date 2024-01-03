package DiceGame.services.impl;

import DiceGame.model.dto.LoginResponse;
import DiceGame.model.exceptions.UserNameDuplicatedException;
import DiceGame.services.IAuthService;
import DiceGame.services.IPlayerService;
import DiceGame.model.dto.AuthLoginRequest;
import DiceGame.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final IPlayerService playerService;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager, IPlayerService playerService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.playerService = playerService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void registerUser(AuthLoginRequest authLoginRequest) throws UserNameDuplicatedException {
        if (playerService.playerExist(authLoginRequest.getUsername())) {
            throw new UserNameDuplicatedException("Username already taken.");
        }
        log.info("User '{}' registered", authLoginRequest.getUsername());
        playerService.createPlayer(authLoginRequest);
    }

    @Override
    public LoginResponse loginUser(AuthLoginRequest authLoginRequest) {
        Authentication auth = getAuthentication(authLoginRequest);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtUtils.generateToken(auth);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(authLoginRequest.getUsername());
        loginResponse.setToken(token);

        log.info("User '{}' logged in.", authLoginRequest.getUsername());
        return loginResponse;
    }

    private Authentication getAuthentication(AuthLoginRequest authLoginRequest) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
               authLoginRequest.getUsername(),
               authLoginRequest.getPassword()
        ));
    }

}
