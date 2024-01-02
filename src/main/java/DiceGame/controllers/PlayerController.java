package DiceGame.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import DiceGame.model.dto.GameDto;
import DiceGame.services.IPlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import DiceGame.model.domain.Game;
import DiceGame.model.dto.PlayerDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/players")
public class PlayerController {
	
	private final IPlayerService playerService;

	public PlayerController(IPlayerService playerService) {
		this.playerService = playerService;
	}

	@PreAuthorize("#id == principal.id or hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<PlayerDto> changePayerName(@PathVariable String id, @RequestParam String name) {
		PlayerDto playerDto = playerService.changePlayerName(id, name);
		return new ResponseEntity<>(playerDto, HttpStatus.OK);
	}

	@PreAuthorize("#id == principal.id or hasRole('ADMIN')")
	@GetMapping("/{id}/games")
	public ResponseEntity<List <GameDto>> getPlayerGames(@PathVariable String id) {
		List <GameDto> games =	playerService.getGamesByPlayerId(id);
		if (games.isEmpty()) {
			log.info("No games for player with id: " + id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
        return new ResponseEntity<>(games, HttpStatus.OK);
	}

	@GetMapping("/ranking")
	public ResponseEntity<Map<String, Double>> getPlayersRanking(){
		Map<String, Double> playersRanking = playerService.getAllPlayersRanking();
		if (playersRanking.isEmpty()) {
			log.info("No Players in the system");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(playersRanking, HttpStatus.OK);
	}
	
	@GetMapping("/ranking/winner")
	public ResponseEntity<PlayerDto> getWinner(){
		Optional<PlayerDto> winnerPlayer = playerService.getWinnerOrLoserPlayer(true);
		if (winnerPlayer.isEmpty()) {
			log.info("No Players in the system");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(winnerPlayer.get(), HttpStatus.OK);
	}
	
	@GetMapping("/ranking/loser")
	public ResponseEntity<PlayerDto> getLoser(){
		Optional<PlayerDto> loserPlayer = playerService.getWinnerOrLoserPlayer(false);
		if (loserPlayer.isEmpty()) {
			log.info("No Players in the system");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(loserPlayer.get(), HttpStatus.OK);
	}

	// Admin Only
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("")
	public ResponseEntity<List<PlayerDto>> listPlayers(){
		List<PlayerDto> playersDto = playerService.getAllPlayers();
		if (playersDto.isEmpty()) {
			log.info("No Players in the system");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(playersDto, HttpStatus.OK);
	}

	@PostMapping("/add-admin-role")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PlayerDto> addAdminRoleToUser(@RequestParam String id) {
		PlayerDto playerDto = playerService.addAdminRole(id);
		log.info("Admin Role added to user {} ", playerDto.getUserName());
		return new ResponseEntity<>(playerDto, HttpStatus.OK);
	}

	@GetMapping("/all-admins")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<PlayerDto>> listAdmins() {
		List<PlayerDto> playersDto = playerService.getAllAdmins();
		if (playersDto.isEmpty()) {
			log.info("No Admins in the system.");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(playersDto, HttpStatus.OK);

	}


}
