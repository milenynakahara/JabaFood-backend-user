package app.jaba.services.validations.user;

import app.jaba.entities.UserEntity;
import app.jaba.exceptions.EmailAlreadyInUseException;
import app.jaba.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailAlreadyInUseValidation implements CreateUserValidation, UpdateUserValidation {

    UserRepository userRepository;

    @Override
    public void validate(UserEntity user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            if (!Objects.equals(u.getId(), user.getId())) {
                throw new EmailAlreadyInUseException("Email already in use");
            }
        });
    }
}
