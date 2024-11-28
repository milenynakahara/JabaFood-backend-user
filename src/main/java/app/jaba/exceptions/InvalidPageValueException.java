package app.jaba.exceptions;

public class InvalidPageValueException extends RuntimeException {
    public InvalidPageValueException(String message) {
        super(message);
    }
}
