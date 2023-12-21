package DiceGame.services.impl;


import java.util.Optional;

import DiceGame.model.domain.Game;
import DiceGame.model.domain.Player;
import DiceGame.model.dto.GameDto;
import DiceGame.model.exceptions.PlayerNotFoundException;
import DiceGame.repository.IPlayerRepository;
import DiceGame.services.IGameService;
import DiceGame.services.IPlayerService;
import DiceGame.utils.mapper.EntityDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameServiceImpl implements IGameService {

	private final IPlayerService playerService;
	private final EntityDtoMapper entityDtoMapper;

	public GameServiceImpl(IPlayerService playerService, EntityDtoMapper entityDtoMapper) {
		this.playerService = playerService;
		this.entityDtoMapper = entityDtoMapper;
	}

	@Override
	public GameDto newGame(String id) {
		Player player = playerService.getPlayerById(id);
		Game game = createNewGame(player);
		updateGameVictories(player, game);
		playerService.savePlayer(player);
		log.info("User {} played a game.", player.getUserName());
		return entityDtoMapper.toGameDto(game);
	}

	@Override
	public void deleteAllGamesByPlayerId(String id) {
		Player player = playerService.getPlayerById(id);
		player.resetPlayer();
		playerService.savePlayer(player);
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

}
