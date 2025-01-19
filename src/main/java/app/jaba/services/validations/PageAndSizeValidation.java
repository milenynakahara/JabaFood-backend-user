package app.jaba.services.validations;

import app.jaba.exceptions.InvalidPageValueException;
import app.jaba.exceptions.InvalidSizeValueException;
import org.springframework.stereotype.Component;

@Component
public class PageAndSizeValidation {

    private static final String VALUE_MUST_BE_GREATER_THAN_OR_EQUAL_TO_0 = "value must be greater than or equal to 0";

    public void validate(int page, int size) {
        if (page < 0) {
            throw new InvalidPageValueException("Page " + VALUE_MUST_BE_GREATER_THAN_OR_EQUAL_TO_0);
        }
        if (size < 0) {
            throw new InvalidSizeValueException("Size " + VALUE_MUST_BE_GREATER_THAN_OR_EQUAL_TO_0);
        }
    }
}
