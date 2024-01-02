package DiceGame.model.services.impl;

import DiceGame.model.dto.LoginResponse;
import DiceGame.services.IPlayerService;
import DiceGame.model.dto.AuthLoginRequest;
import DiceGame.model.dto.PlayerDto;
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

import java.time.LocalDateTime;
import java.util.Date;

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
                .userName("test@gmail.com")
                .password("testpswd")
                .build();
    }

    @Test
    void shouldRegisterUser(){
        //given
        PlayerDto playerDto = PlayerDto.builder()
                .id("id")
                .name(authLoginRequest.getName())
                .userName(authLoginRequest.getUserName())
                .registrationDate(LocalDateTime.now())
                .winSuccess(0)
                .build();

        Mockito.when(playerService.playerExist(authLoginRequest.getUserName())).thenReturn(false);
        Mockito.when(playerService.createPlayer(authLoginRequest)).thenReturn(playerDto);

        //when
        PlayerDto result = authServiceImpl.registerUser(authLoginRequest);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUserName()).isEqualTo(authLoginRequest.getUserName());
    }

    @Test
    void shouldNotRegisterUser_UserNameTaken(){
        //given
        Mockito.when(playerService.playerExist(authLoginRequest.getUserName())).thenReturn(true);

        //when
        PlayerDto result = authServiceImpl.registerUser(authLoginRequest);

        //then
        Assertions.assertThat(result).isNull();

    }

    @Test
    void shouldLoginUser(){
        //given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authLoginRequest.getUserName(),
                authLoginRequest.getPassword());

        String token = "testToken";

        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class)))
                .thenReturn(authentication);
        Mockito.when(jwtUtils.generateToken(authentication)).thenReturn(token);

        //when
       LoginResponse result = authServiceImpl.loginUser(authLoginRequest);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getToken()).isEqualTo(token);

    }

    @Test
    void shouldNotLoginUser_throwsException(){
        //given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authLoginRequest.getUserName(),
                authLoginRequest.getPassword());
        String token = "testToken";

        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class)))
                .thenThrow(new AuthenticationException("Credenciales incorrectas"){});

        //when
        LoginResponse result = authServiceImpl.loginUser(authLoginRequest);

        //then
        Assertions.assertThat(result).isNull();


    }
}