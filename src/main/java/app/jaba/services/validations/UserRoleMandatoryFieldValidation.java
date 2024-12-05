package app.jaba.services.validations;

import app.jaba.entities.UserRoleEntity;
import app.jaba.exceptions.SaveUserRoleException;
import org.springframework.stereotype.Component;

@Component
public class UserRoleMandatoryFieldValidation implements CreateUserRoleValidation {
    @Override
    public void validate(UserRoleEntity userRoleEntity) {
        if (userRoleEntity.getUserId() == null || userRoleEntity.getRoleId() == null) {
            throw new SaveUserRoleException("User ID and Role ID are mandatory fields.");
        }
    }
}
