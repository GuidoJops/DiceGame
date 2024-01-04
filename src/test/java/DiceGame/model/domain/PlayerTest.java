package DiceGame.model.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = Player.builder()
                .winSuccess(50.0)
                .victories(2)
                .games(List.of(new Game(), new Game(), new Game(), new Game())) //4 games
                .build();
    }

    @Test
    void shouldResetPlayer() {
        player.resetPlayer();

        Assertions.assertThat(player.getWinSuccess()).isZero();
        Assertions.assertThat(player.getVictories()).isZero();
        Assertions.assertThat(player.getGames()).isEmpty();

    }

    @Test
    void shouldCalculateWinSuccess(){
        double winSuccess = player.winSuccessCalculator();

        Assertions.assertThat(winSuccess).isEqualTo(player.getWinSuccess());
    }

}