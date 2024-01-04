package DiceGame.model.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(game.getDiceA()).isPositive();
        assertThat(game.getDiceB()).isPositive();
        assertThat(game).hasFieldOrProperty("win");
    }

    @Test
    void shouldReturnTrueIfGameIsWin() {
        game.setDiceA(2);
        game.setDiceB(5);
        game.setWin(true);
        assertThat(game.isWin()).isTrue();
    }
}
