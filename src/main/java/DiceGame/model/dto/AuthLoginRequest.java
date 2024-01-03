package DiceGame.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthLoginRequest {

    private String name = "NoNamePlayer"; // Default name

    @Email(message = "Username must be an e-mail format. 'xx@mail.com.'")
    @NotBlank (message = "Username must be at least one character.")
    private String username;

    @NotBlank (message = "Password must be at least one character.")
    private String password;


}
