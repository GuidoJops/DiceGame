package DiceGame.services;

import DiceGame.model.dto.GameDto;

public interface IGameService {
	
	void deleteAllGamesByPlayerId(String id);
	GameDto newGame(String id);

}
