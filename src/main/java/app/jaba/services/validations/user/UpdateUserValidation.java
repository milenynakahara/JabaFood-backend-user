package app.jaba.services.validations.user;

import app.jaba.entities.UserEntity;

public interface UpdateUserValidation {
    void validate(UserEntity user);
}
