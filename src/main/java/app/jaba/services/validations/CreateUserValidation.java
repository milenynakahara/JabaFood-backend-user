package app.jaba.services.validations;

import app.jaba.entities.UserEntity;

public interface CreateUserValidation {

    void validate(UserEntity user);
}
