package app.jaba.exceptions;

public class EmailFormatException extends RuntimeException {
    public EmailFormatException(String message) {
        super(message);
    }
}
