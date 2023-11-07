package DiceGame.model.services;

import DiceGame.model.dto.AuthResponse;
import DiceGame.model.dto.AuthRequest;
import DiceGame.model.dto.PlayerDto;

public interface IAuthService {
    PlayerDto registerUser(AuthRequest authRequest);
    AuthResponse loginUser(AuthRequest authRequest);

}
