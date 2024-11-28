package app.jaba.services.validations;

import app.jaba.exceptions.InvalidPageValueException;
import app.jaba.exceptions.InvalidSizeValueException;
import org.springframework.stereotype.Component;

@Component
public class PageAndSizeValidation {

    public void validate(int page, int size) {
        if (page < 1) {
            throw new InvalidPageValueException("Page value must be greater than 0");
        }
        if (size < 0) {
            throw new InvalidSizeValueException("Size value must be greater than or equal to 0");
        }
    }
}
