package DiceGame.repository;

import DiceGame.model.domain.Game;
import DiceGame.model.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class PlayerRepositoryTest {

    @Autowired
    private IPlayerRepository playerRepository;

    private Player player;

    @BeforeEach
    void setUp() {
        player = Player.builder()
                .id("playerId")
                .name("testPlayer")
                .userName("testplayer@mail.com")
                .winSuccess(50.0)
                .victories(2)
                .games(List.of(new Game(), new Game(), new Game(), new Game())) //4 games
                .build();

        playerRepository.save(player);
    }

    @Test
    void shouldFindByUsername() {
        Optional<Player> retrievedPlayer = playerRepository.findByUserName(player.getUserName());

        assertThat(retrievedPlayer).isPresent();
        assertThat(retrievedPlayer.get().getName()).isEqualTo(player.getName());
        assertThat(retrievedPlayer.get().getUserName()).isEqualTo(player.getUserName());
    }

    @Test
    void shouldNotFindByUsername() {
        Optional<Player> retrievedPlayer = playerRepository.findByUserName("fakeName");

        assertThat(retrievedPlayer).isEmpty();
    }

    @Test
    void shouldReturnTrueIfExistsByUserName() {
        boolean exist = playerRepository.existsByUserName(player.getUserName());

        assertThat(exist).isTrue();
    }

    @Test
    void shouldReturnFalseIfExistsByUserName() {
        boolean exist = playerRepository.existsByUserName("fakeName");

        assertThat(exist).isFalse();
    }
}
