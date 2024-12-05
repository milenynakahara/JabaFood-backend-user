package app.jaba.services.validations;

import app.jaba.entities.UserEntity;

public interface UpdateUserValidation {
    void validate(UserEntity user);
}
