package DiceGame.model.exceptions;

public class UserNameDuplicatedException extends RuntimeException {

    public UserNameDuplicatedException(String message) {
        super(message);
    }
}
