package app.jaba.services.validations.user;

import app.jaba.entities.UserEntity;
import app.jaba.exceptions.EmailFormatException;
import org.springframework.stereotype.Component;

@Component
public class EmailFormatValidation implements CreateUserValidation, UpdateUserValidation {

    @Override
    public void validate(UserEntity user) {
        if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new EmailFormatException("Invalid email format");
        }
    }
}
