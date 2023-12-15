package DiceGame.controllers;

import DiceGame.model.dto.GameDto;
import DiceGame.services.IGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	private final IGameService gameService;

	public GameController(IGameService gameService) {
		this.gameService = gameService;
	}

	@PostMapping("players/{id}/games")
	@PreAuthorize("#id == principal.id")
	public ResponseEntity<GameDto> playGame(@PathVariable String id) {
		GameDto gameDto = gameService.newGame(id);
        return new ResponseEntity<>(gameDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("players/{id}/games")
	@PreAuthorize("hasRole ('ADMIN')")
	public ResponseEntity<String> deleteGames(@PathVariable String id) {
		gameService.deleteAllGamesByPlayerId(id);
        return new ResponseEntity<>("Games removed from Player with id: " + id, HttpStatus.OK);


	}
	

}
