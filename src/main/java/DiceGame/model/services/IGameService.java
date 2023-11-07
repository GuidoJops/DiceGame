package DiceGame.model.services;

import DiceGame.model.dto.GameDto;

public interface IGameService {
	
	boolean deleteAllGamesByPlayerId(String id);
	GameDto newGame(String id);

}
