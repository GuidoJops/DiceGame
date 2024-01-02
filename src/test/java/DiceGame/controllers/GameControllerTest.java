package DiceGame.controllers;

import DiceGame.model.dto.GameDto;
import DiceGame.services.IGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IGameService gameService;


    @Test
    void shouldCreateGame() throws Exception {
        GameDto gameDto = GameDto.builder()
                .diceA(2)
                .diceB(3)
                .win(false)
                .build();

        when(gameService.newGame(anyString())).thenReturn(gameDto);

        mockMvc.perform(post("/games/id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameDto)))
                .andExpect(jsonPath("$.diceA").value(gameDto.getDiceA()))
                .andExpect(jsonPath("$.diceB").value(gameDto.getDiceB()))
                .andExpect(jsonPath("$.win").value(gameDto.isWin()))
                .andExpect(status().isCreated());

        verify(gameService, times(1)).newGame(anyString());
    }

    @Test
    void shouldDeleteAllPlayerGames() throws Exception {
        doNothing().when(gameService).deleteAllGamesByPlayerId(anyString());

        mockMvc.perform(delete("/games/id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(gameService, times(1)).deleteAllGamesByPlayerId(anyString());
    }
}
