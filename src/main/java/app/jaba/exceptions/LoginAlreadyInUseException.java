package app.jaba.exceptions;

public class LoginAlreadyInUseException extends RuntimeException {
    public LoginAlreadyInUseException(String message) {
        super(message);
    }
}
