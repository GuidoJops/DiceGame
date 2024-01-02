package DiceGame.controllers;

import DiceGame.model.dto.GameDto;
import DiceGame.services.IGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {

	private final IGameService gameService;

	public GameController(IGameService gameService) {
		this.gameService = gameService;
	}

	@PostMapping("/{id}")
	@PreAuthorize("#id == principal.id")
	public ResponseEntity<GameDto> playGame(@PathVariable String id) {
		GameDto gameDto = gameService.newGame(id);
        return new ResponseEntity<>(gameDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole ('ADMIN')")
	public ResponseEntity<String> deleteGames(@PathVariable String id) {
		gameService.deleteAllGamesByPlayerId(id);
        return new ResponseEntity<>("Games removed from Player with id: " + id, HttpStatus.OK);
	}
	

}
