package DiceGame.utils.mapper;

import org.springframework.stereotype.Component;
import DiceGame.model.domain.Game;
import DiceGame.model.domain.Player;
import DiceGame.model.dto.GameDto;
import DiceGame.model.dto.PlayerDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EntityDtoMapper {

	// PLAYER
	public PlayerDto toPlayerDto(Player player) {
		PlayerDto playerDto = new PlayerDto();

		playerDto.setId(player.getId());
		playerDto.setName(player.getName());
		playerDto.setUserName(player.getUserName());
		playerDto.setRegistDate(player.getRegistDate());
		playerDto.setWinSuccess(player.getWinSuccess());

		return playerDto;

	}

	// GAME
	public GameDto toGameDto(Game game) {
		GameDto gameDto = new GameDto();

		gameDto.setDiceA(game.getDiceA());
		gameDto.setDiceB(game.getDiceB());
		gameDto.setWin(game.isWin());

		return gameDto;
	}
	
}
