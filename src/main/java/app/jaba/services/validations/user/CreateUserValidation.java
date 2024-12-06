package app.jaba.services.validations.user;

import app.jaba.entities.UserEntity;

public interface CreateUserValidation {

    void validate(UserEntity user);
}
