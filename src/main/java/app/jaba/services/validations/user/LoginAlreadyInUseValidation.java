package app.jaba.services.validations.user;

import app.jaba.entities.UserEntity;
import app.jaba.exceptions.LoginAlreadyInUseException;
import app.jaba.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LoginAlreadyInUseValidation implements CreateUserValidation, UpdateUserValidation {

    UserRepository userRepository;

    @Override
    public void validate(UserEntity user) {
        userRepository.findByLogin(user.getLogin()).ifPresent(u -> {
            if (!Objects.equals(u.getId(), user.getId())) {
                throw new LoginAlreadyInUseException("Login already in use");
            }
        });
    }
}
