package DiceGame.model.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void shouldCreateGameWithEmptyArguments() {
        Assertions.assertThat(game.getDiceA()).isPositive();
        Assertions.assertThat(game.getDiceB()).isPositive();
        Assertions.assertThat(game).hasFieldOrProperty("win");
    }

    @Test
    void shouldReturnTrueIfGameIsWin() {
        game.setDiceA(2);
        game.setDiceB(5);
        game.setWin(true);
        Assertions.assertThat(game.isWin()).isTrue();
    }
}
