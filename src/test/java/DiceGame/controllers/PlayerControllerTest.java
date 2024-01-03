package DiceGame.controllers;

import DiceGame.model.dto.GameDto;
import DiceGame.model.exceptions.PlayerNotFoundException;
import DiceGame.services.IPlayerService;
import DiceGame.model.dto.PlayerDto;
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

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.CoreMatchers.is;

@WebMvcTest(controllers = PlayerController.class)
@AutoConfigureMockMvc (addFilters = false)
@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IPlayerService playerService;

    private PlayerDto playerDto1;
    private PlayerDto playerDto2;
    private GameDto gameDto1;
    private GameDto gameDto2;
    private List<PlayerDto> playersDto;


    @BeforeEach
    void setUp() {
        gameDto1 = GameDto.builder()
                .diceA(1)
                .diceB(6)
                .win(true)
                .build();

        gameDto2 = GameDto.builder()
                .diceA(1)
                .diceB(3)
                .win(false)
                .build();

        playerDto1 = PlayerDto.builder()
                .id("id1")
                .name("player1")
                .username("player1@mail.com")
                .registrationDate(LocalDateTime.now())
                .winSuccess(100.0)
                .build();

        playerDto2 = PlayerDto.builder()
                .id("id2")
                .name("player2")
                .username("player2@mail.com")
                .registrationDate(LocalDateTime.now())
                .winSuccess(0)
                .build();

        playersDto = new ArrayList<>(List.of(playerDto1, playerDto2));
    }

    @Test
    void shouldChangePlayerName() throws Exception {
        when(playerService.changePlayerName(anyString(),anyString())).thenReturn(playerDto1);

        mockMvc.perform(put("/players/id")
                        .param("name", "testName") //simulating a string as url param
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDto1)))
                .andExpect(jsonPath("$.id").value(playerDto1.getId()))
                .andExpect(jsonPath("$.name").value(playerDto1.getName()))
                .andExpect(jsonPath("$.winSuccess").value(playerDto1.getWinSuccess()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(playerService, times(1)).changePlayerName(anyString(), anyString());
    }

    @Test
    void shouldNotChangePlayerName() throws Exception {
        when(playerService.changePlayerName(anyString(), anyString())).thenThrow(new PlayerNotFoundException("Player not found"));

        mockMvc.perform(put("/players/id")
                        .param("name", "testName") //simulating a string as url param
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(playerService, times(1)).changePlayerName(anyString(), anyString());
    }

    @Test
    void shouldGetPlayerGames() throws Exception {
        List<GameDto> games = Arrays.asList(gameDto1,gameDto2);
        when(playerService.getGamesByPlayerId(anyString())).thenReturn(games);

        mockMvc.perform(get("/players/id/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(games.size())))
                .andExpect(status().isOk());

        verify(playerService, times(1)).getGamesByPlayerId(anyString());
    }

    @Test
    void shouldNotGetPlayerGames() throws Exception {
        when(playerService.getGamesByPlayerId(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/players/id/games")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(playerService, times(1)).getGamesByPlayerId(anyString());
    }

    @Test
    void shouldGetPlayersRanking() throws Exception {
        Map<String, Double> playersRanking = new HashMap<>();
        playersRanking.put("juan", 23.3);
        playersRanking.put("maria", 50.0);

        when(playerService.getAllPlayersRanking()).thenReturn(playersRanking);

       mockMvc.perform(get("/players/ranking")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(playersRanking)))
               .andExpect(jsonPath("$", aMapWithSize(playersRanking.size())))
               .andExpect(status().isOk());

        verify(playerService, times(1)).getAllPlayersRanking();
    }

    @Test
    void shouldNotGetPlayersRanking() throws Exception {
        when(playerService.getAllPlayersRanking()).thenReturn(new HashMap<>());

        mockMvc.perform(get("/players/ranking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(playerService, times(1)).getAllPlayersRanking();
    }

    @Test
    void shouldGetWinnerPlayer() throws Exception {
        when(playerService.getWinnerOrLoserPlayer(true)).thenReturn(Optional.of(playerDto1));

        mockMvc.perform(get("/players/ranking/winner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDto1)))
                .andExpect(jsonPath("$.name", is(playerDto1.getName())))
                .andExpect(status().isOk());

        verify(playerService, times(1)).getWinnerOrLoserPlayer(true);
    }

    @Test
    void shouldGetLoserPlayer() throws Exception {
        when(playerService.getWinnerOrLoserPlayer(false)).thenReturn(Optional.of(playerDto2));

        mockMvc.perform(get("/players/ranking/loser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDto2)))
                .andExpect(jsonPath("$.name", is(playerDto2.getName())))
                .andExpect(status().isOk());

        verify(playerService, times(1)).getWinnerOrLoserPlayer(false);
    }

    @Test
    void shouldGetAllPlayers() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(playersDto);

        mockMvc.perform(get("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playersDto)))
                .andExpect(jsonPath("$", hasSize(playersDto.size())))
                .andExpect(status().isOk());

        verify(playerService, times(1)).getAllPlayers();

    }

    @Test
    void shouldNotGetAllPlayers() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(playerService, times(1)).getAllPlayers();

    }

    @Test
    void shouldAddAdminRoleToUser() throws Exception {
        when(playerService.addAdminRole(anyString())).thenReturn(playerDto1);

        mockMvc.perform(post("/players/add-admin-role")
                .param("id", "id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerDto1)))
                .andExpect(jsonPath("$.name").value(playerDto1.getName()))
                .andExpect(status().isOk());

        verify(playerService, times(1)).addAdminRole(anyString());
    }

    @Test
    void shouldNoAddAdminRoleToUser() throws Exception {
        when(playerService.addAdminRole(anyString())).thenThrow(new PlayerNotFoundException("Player not found"));

        mockMvc.perform(post("/players/add-admin-role")
                        .param("id", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(playerService, times(1)).addAdminRole(anyString());
    }

    @Test
    void shouldGetAllAdmins() throws Exception {
        when(playerService.getAllAdmins()).thenReturn(playersDto);

        mockMvc.perform(get("/players/all-admins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(playerService, times(1)).getAllAdmins();

    }

    @Test
    void shouldNotGetAllAdmins() throws Exception {
        when(playerService.getAllAdmins()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/players/all-admins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(playerService, times(1)).getAllAdmins();

    }

}