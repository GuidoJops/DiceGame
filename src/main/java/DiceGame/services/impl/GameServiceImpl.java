package DiceGame.services.impl;


import java.util.Optional;

import DiceGame.model.domain.Game;
import DiceGame.model.domain.Player;
import DiceGame.model.dto.GameDto;
import DiceGame.model.exceptions.PlayerNotFoundException;
import DiceGame.repository.IPlayerRepository;
import DiceGame.services.IGameService;
import DiceGame.utils.mapper.EntityDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameServiceImpl implements IGameService {

	private final IPlayerRepository playerRepository;
	private final EntityDtoMapper entityDtoMapper;

	public GameServiceImpl(IPlayerRepository playerRepository, EntityDtoMapper entityDtoMapper) {
		this.playerRepository = playerRepository;
		this.entityDtoMapper = entityDtoMapper;
	}

	@Override
	public GameDto newGame(String id) throws PlayerNotFoundException {
		Player player = getPlayer(id);
		Game game = createNewGame(player);
		updateGameVictories(player, game);
		playerRepository.save(player);
		log.info("User {} played a game.", player.getUserName());
		return entityDtoMapper.toGameDto(game);
	}

	@Override
	public void deleteAllGamesByPlayerId(String id) {
		Player player = getPlayer(id);
		player.resetPlayer();
		playerRepository.save(player);
	}

	private Game createNewGame(Player player) {
		Game game = new Game();
		player.getGames().add(game);
		return game;
	}

	private void updateGameVictories(Player player, Game game) {
		if (game.isWin()) {
			player.setVictories(player.getVictories() + 1);
		}
		player.updateWinSuccess();
	}

	private Player getPlayer(String id) throws PlayerNotFoundException {
		Optional<Player> oPlayer = playerRepository.findById(id);
		return oPlayer.orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + id));

	}

}
