package DiceGame.services.impl;

import java.util.*;
import java.util.function.Predicate;

import DiceGame.model.dto.GameDto;
import DiceGame.model.exceptions.PlayerNotFoundException;
import DiceGame.repository.IPlayerRepository;
import DiceGame.repository.IRoleRepository;
import DiceGame.services.IPlayerService;
import DiceGame.utils.mapper.EntityDtoMapper;
import DiceGame.model.domain.ERole;
import DiceGame.model.domain.Role;
import DiceGame.model.dto.AuthLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import DiceGame.model.domain.Game;
import DiceGame.model.domain.Player;
import DiceGame.model.dto.PlayerDto;


@Slf4j
@Service
public class PlayerServiceImpl implements IPlayerService {

	private final IPlayerRepository playerRepository;
	private final EntityDtoMapper entityDtoMapper;
	private final IRoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public PlayerServiceImpl(IPlayerRepository playerRepository,
							 EntityDtoMapper entityDtoMapper,
							 IRoleRepository roleRepository,
							 PasswordEncoder passwordEncoder) {
		this.playerRepository = playerRepository;
		this.entityDtoMapper = entityDtoMapper;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}


	@Override
	public PlayerDto createPlayer(AuthLoginRequest authLoginRequest) {
		Player player = new Player(authLoginRequest.getName(), authLoginRequest.getUserName(),
				passwordEncoder.encode(authLoginRequest.getPassword()));
		log.info("Player '{}' created", player.getUserName());
		return entityDtoMapper.toPlayerDto(savePlayer(player));
	}

	@Override
	public PlayerDto changePlayerName(String id, String name) {
		Player player = getPlayerById(id);
		player.setName(name);
		return entityDtoMapper.toPlayerDto(savePlayer(player));
	}

	@Override
	public List<GameDto> getGamesByPlayerId (String id){
		Player player = getPlayerById(id);
		return player.getGames().stream()
				.map(entityDtoMapper::toGameDto)
				.toList();
	}

	@Override
	public Map<String, Double> getAllPlayersRanking() {
		// Excludes 'NoNamePLayer' and 'DEFAULT-ADMIN'
		List<Player> players = listPlayersByRankingDesc();
		return playersRankingCreator(players);
	}

	@Override
	public Optional<PlayerDto> getWinnerOrLoserPlayer(boolean isWinner) {
		List<Player> players = listPlayersByRankingDesc();
		if (players.isEmpty()) {
			return Optional.empty();
		}
		Player player = isWinner ? players.get(0) : players.get(players.size() - 1);
		return Optional.of(entityDtoMapper.toPlayerDto(player));
	}

	@Override
	public List<PlayerDto> getAllPlayers() {
		// ADMIN Roles filtered
		return playerRepository.findAll()
				.stream()
				.filter(Predicate.not(p->p.getName().equals("DEFAULT-ADMIN")))
				.map(entityDtoMapper::toPlayerDto)
				.toList();
	}

	@Override
	public PlayerDto addAdminRole(String id) {
		Player player = getPlayerById(id);
		List<Role> roles = player.getRoles();
		roles.add(roleRepository.findByType(ERole.ROLE_ADMIN).get());
		return entityDtoMapper.toPlayerDto(savePlayer(player));
	}

	@Override
	public List<PlayerDto> getAllAdmins() {
		return playerRepository.findAll()
				.stream()
				.filter(player -> player.getRoles()
						.stream()
						.anyMatch(role -> role.getType().equals(ERole.ROLE_ADMIN)))
				.map(entityDtoMapper::toPlayerDto)
				.toList();
	}

	@Override
	public boolean playerExist(String userName) {
		return playerRepository.existsByUserName(userName);
	}

	@Override
	public Player getPlayerById(String id) throws PlayerNotFoundException {
		Optional<Player> oPlayer = playerRepository.findById(id);
		return oPlayer.orElseThrow(() -> new PlayerNotFoundException("Player with ID: " + id + " not found."));
	}

	@Override
	public Player savePlayer(Player player) {
		return playerRepository.save(player);
	}

	private List<Player> listPlayersByRankingDesc() {
		return playerRepository.findAll()
				.stream()
				.filter(player -> !isExcludedPlayer(player)) //Excludes 'NoNamePLayer' and 'DEFAULT-ADMIN'
				.sorted(Comparator.comparing(Player::getWinSuccess).reversed())
				.toList();
	}

	private Map<String, Double> playersRankingCreator(List<Player> players) {
		Map<String, Double> rankingMap = new LinkedHashMap<>();
		players.forEach(p -> rankingMap.put(p.getUserName(), p.getWinSuccess()));
		return rankingMap;
	}

	private boolean isExcludedPlayer(Player player) {
		return player.getName().equalsIgnoreCase("NoNamePlayer") ||
				player.getName().equalsIgnoreCase("DEFAULT-ADMIN");
	}
}
