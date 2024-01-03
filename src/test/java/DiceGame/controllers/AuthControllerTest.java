package DiceGame.controllers;

import DiceGame.model.dto.AuthLoginRequest;
import DiceGame.model.dto.LoginResponse;
import DiceGame.services.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthService authService;

    private AuthLoginRequest authLoginRequest;

    @BeforeEach
    void setUp() {
        authLoginRequest = AuthLoginRequest.builder()
                .name("user1")
                .username("user1@mail.com")
                .password("123")
                .build();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        doNothing().when(authService).registerUser(any(AuthLoginRequest.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authLoginRequest)))
                .andExpect(content().string("Successfully registered"))
                .andExpect(status().isCreated());

        verify(authService, times(1)).registerUser(any(AuthLoginRequest.class));

    }

    @Test
    void shouldLoginUser() throws Exception {
        LoginResponse loginResponse = LoginResponse.builder()
                .username("user1")
                .token("accessToken")
                .build();

        when(authService.loginUser(any(AuthLoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform((post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authLoginRequest))))
                .andExpect(jsonPath("$.username").value(loginResponse.getUsername()))
                .andExpect(jsonPath("$.token").value(loginResponse.getToken()))
                .andExpect(status().isOk());

        verify(authService, times(1)).loginUser(any(AuthLoginRequest.class));

    }

}
