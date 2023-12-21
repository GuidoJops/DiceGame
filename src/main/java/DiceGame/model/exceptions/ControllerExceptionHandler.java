package DiceGame.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.relation.RoleNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserNameDuplicatedException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String playerDuplicatedExceptionHandler (UserNameDuplicatedException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String AuthenticationExceptionHandler (AuthenticationException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String PlayerNotFoundExceptionHandler (PlayerNotFoundException exception) {
        return exception.getMessage();
    }

}
