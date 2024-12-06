package app.jaba.services.validations.user;

import app.jaba.entities.UserEntity;
import app.jaba.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CheckLoginUpdateValidation implements UpdateUserValidation {

    UserRepository userRepository;

    @Override
    public void validate(UserEntity user) {
        // TODO: VALIDAR SE O LOGIN É O MESMO DURANTE ATUALIZACAO E NAO PODE SER DIFERENTE
    }
}
