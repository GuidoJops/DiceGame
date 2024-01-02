package DiceGame.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import DiceGame.model.domain.Game;
import DiceGame.model.domain.Player;
import DiceGame.model.dto.AuthLoginRequest;
import DiceGame.model.dto.GameDto;
import DiceGame.model.dto.PlayerDto;

public interface IPlayerService {
	List<PlayerDto> getAllPlayers();
	PlayerDto createPlayer(AuthLoginRequest authLoginRequest);
	Map<String, Double> getAllPlayersRanking();
	List<GameDto> getGamesByPlayerId(String id);
	Optional<PlayerDto> getWinnerOrLoserPlayer(boolean isWinner);
	boolean playerExist(String userName);
	PlayerDto changePlayerName(String id, String Name);
	PlayerDto addAdminRole(String id);
	List<PlayerDto> getAllAdmins();
	Player getPlayerById(String id);
	Player savePlayer(Player player);

}
