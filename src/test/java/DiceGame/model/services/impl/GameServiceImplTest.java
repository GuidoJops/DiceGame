package DiceGame.model.services.impl;

import DiceGame.model.dto.GameDto;
import DiceGame.services.IPlayerService;
import DiceGame.services.impl.GameServiceImpl;
import DiceGame.utils.mapper.EntityDtoMapper;
import DiceGame.model.domain.Game;
import DiceGame.model.domain.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private IPlayerService playerService;
    @Mock
    private EntityDtoMapper entityDtoMapper;

    @InjectMocks
    private GameServiceImpl gameServiceImpl;

    private Player player;
    private Game game;


    @BeforeEach
    void setUp(){
        game = new Game();
        game.setWin(true);
        game.setDiceA(6);
        game.setDiceB(1);

        List<Game> games = new ArrayList<>();
        games.add(game);

        player = Player.builder()
                .id("id")
                .userName("userName")
                .games(games)
                .victories(1)
                .build();
    }

    @Test
    void shouldCreateNewGame(){
        Mockito.when(playerService.getPlayerById(Mockito.any(String.class))).thenReturn(player);
        Mockito.when(entityDtoMapper.toGameDto(Mockito.any(Game.class)))
                .thenAnswer(invocation -> {
                    Game invGame = invocation.getArgument(0);
                    return GameDto.builder()
                            .diceA(invGame.getDiceA())
                            .diceB(invGame.getDiceB())
                            .win(invGame.isWin())
                            .build();
                });

       GameDto result = gameServiceImpl.newGame(player.getId());

        Assertions.assertThat(result).isNotNull();
        Mockito.verify(playerService, times(1)).savePlayer(Mockito.any(Player.class));
    }

    @Test
    void shouldDeleteAllGamesByPlayerId(){
        Mockito.when(playerService.getPlayerById(Mockito.any(String.class))).thenReturn(player);

        gameServiceImpl.deleteAllGamesByPlayerId(player.getId());

        Assertions.assertThat(player.getGames().size()).isEqualTo(0);
        Mockito.verify(playerService, times(1)).savePlayer(Mockito.any(Player.class));
    }

    //More tests pending...

}