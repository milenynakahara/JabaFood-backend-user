package app.jaba.services.validations;

import app.jaba.entities.UserRoleEntity;

public interface CreateUserRoleValidation {
    void validate(UserRoleEntity userRoleEntity);
}
