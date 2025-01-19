package app.jaba.exceptions;

public class AddressMandatoryFieldException extends RuntimeException {
    public AddressMandatoryFieldException(String message) {
        super(message);
    }
}
