package DiceGame.services;

import DiceGame.model.dto.LoginResponse;
import DiceGame.model.dto.AuthLoginRequest;

public interface IAuthService {
    void registerUser(AuthLoginRequest authLoginRequest);
    LoginResponse loginUser(AuthLoginRequest authLoginRequest);

}
