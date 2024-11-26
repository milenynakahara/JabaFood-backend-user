package app.jaba.exceptions;

public class UserMandatoryFieldException extends RuntimeException {
    public UserMandatoryFieldException(String message) {
        super(message);
    }
}
