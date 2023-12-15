package DiceGame.services;

import DiceGame.model.dto.LoginResponse;
import DiceGame.model.dto.AuthLoginRequest;
import DiceGame.model.dto.PlayerDto;

public interface IAuthService {
    PlayerDto registerUser(AuthLoginRequest authLoginRequest);
    LoginResponse loginUser(AuthLoginRequest authLoginRequest);

}
