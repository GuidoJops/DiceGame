package DiceGame.services;

import java.util.List;
import java.util.Map;
import DiceGame.model.domain.Game;
import DiceGame.model.dto.AuthLoginRequest;
import DiceGame.model.dto.PlayerDto;

public interface IPlayerService {
	List<PlayerDto> getAllPlayers();
	PlayerDto createPlayer(AuthLoginRequest authLoginRequest);
	Map<String, Double> getAllPlayersRanking();
	PlayerDto getPlayerWinner();
	PlayerDto getPlayerLoser();
	PlayerDto findPlayerById(String id);
	List<Game> getGamesByPlayerId(String id);
	boolean playerExist(String userName);
	PlayerDto changePlayerName(String id, String Name);
	PlayerDto addAdminRole(String id);
	List<PlayerDto> getAllAdmins();

}