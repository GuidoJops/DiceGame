package DiceGame.model.services.impl;

import DiceGame.model.dto.LoginResponse;
import DiceGame.model.exceptions.UserNameDuplicatedException;
import DiceGame.services.IPlayerService;
import DiceGame.model.dto.AuthLoginRequest;
import DiceGame.security.jwt.JwtUtils;
import DiceGame.services.impl.AuthServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private IPlayerService playerService;
    @Mock private JwtUtils jwtUtils;

    @InjectMocks private AuthServiceImpl authServiceImpl;

    private AuthLoginRequest authLoginRequest;

    @BeforeEach
    void setUp(){
        authLoginRequest = AuthLoginRequest.builder()
                .name("testname")
                .username("test@gmail.com")
                .password("testpswd")
                .build();
    }

    @Test
    void shouldRegisterUser(){
        Mockito.when(playerService.playerExist(Mockito.any(String.class))).thenReturn(false);
        authServiceImpl.registerUser(authLoginRequest);
        Mockito.verify(playerService, times(1)).createPlayer(Mockito.any(AuthLoginRequest.class));
    }

    @Test
    void shouldNotRegisterUser_UserNameTaken(){
        Mockito.when(playerService.playerExist(Mockito.any(String.class))).thenReturn(true);

        Assertions.assertThatThrownBy(() -> authServiceImpl.registerUser(authLoginRequest))
                .isInstanceOf(UserNameDuplicatedException.class)
                .hasMessage("Username already taken.");

        Mockito.verify(playerService, never()).createPlayer(Mockito.any(AuthLoginRequest.class));
    }

    @Test
    void shouldLoginUser(){
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authLoginRequest.getUsername(),
                authLoginRequest.getPassword());

        String token = "testToken";

        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(authentication);
        Mockito.when(jwtUtils.generateToken(authentication)).thenReturn(token);

       LoginResponse result = authServiceImpl.loginUser(authLoginRequest);

        Assertions.assertThat(result.getToken()).isEqualTo(token);
        Assertions.assertThat(result.getUsername()).isEqualTo(authLoginRequest.getUsername());
    }

    @Test
    void shouldNotLoginUser_throwsException(){
        authLoginRequest.setUsername("invalidUser");
        authLoginRequest.setPassword("invalidPassword");
        String exceptMsg = "Bad Credentials";

        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class)))
                .thenThrow(new AuthenticationException(exceptMsg){});

        Assertions.assertThatThrownBy(() -> authServiceImpl.loginUser(authLoginRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Bad Credentials");
    }

    //More tests pending...
}