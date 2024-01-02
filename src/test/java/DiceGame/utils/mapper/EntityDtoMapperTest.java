package DiceGame.utils.mapper;

import DiceGame.model.dto.GameDto;
import DiceGame.model.domain.ERole;
import DiceGame.model.domain.Game;
import DiceGame.model.domain.Player;
import DiceGame.model.domain.Role;
import DiceGame.model.dto.PlayerDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@SpringBootTest
class EntityDtoMapperTest {

    @Autowired
    private EntityDtoMapper entityDtoMapper;


    @Test
    void shouldReturnPlayerDto() {
        //given
        Player player = Player.builder()
                .id("id")
                .name("testName")
                .userName("test@gmail.com")
                .password("testpswd")
                .registrationDate(LocalDateTime.now())
                .winSuccess(33)
                .victories(1)
                .games(Arrays.asList(new Game()))
                .roles(Arrays.asList(new Role(2l, ERole.ROLE_USER)))
                .build();

//        //when
        PlayerDto result = entityDtoMapper.toPlayerDto(player);

        //then
        Assertions.assertThat(result.getName()).isEqualTo(player.getName());
        Assertions.assertThat(result.getUserName()).isEqualTo(player.getUserName());
        Assertions.assertThat(result.getWinSuccess()).isEqualTo(player.getWinSuccess());
    }

    @Test
    void shouldReturnGameDto() {
        //given
        Game game = new Game();

        //when
        GameDto result = entityDtoMapper.toGameDto(game);

        //then
        Assertions.assertThat(result.getDiceA()).isEqualTo(game.getDiceA());
        Assertions.assertThat(result.getDiceB()).isEqualTo(game.getDiceB());
        Assertions.assertThat(result.isWin()).isEqualTo(game.isWin());

    }
}