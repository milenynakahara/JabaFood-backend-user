package app.jaba.services.validations.user;

import app.jaba.entities.UserEntity;
import app.jaba.exceptions.UserMandatoryFieldException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserMandatoryFieldValidation implements CreateUserValidation, UpdateUserValidation {

    @Override
    public void validate(UserEntity user) {
        if (!StringUtils.hasText(user.getName())) {
            throw new UserMandatoryFieldException("Name is mandatory");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new UserMandatoryFieldException("Email is mandatory");
        }
        if (!StringUtils.hasText(user.getLogin())) {
            throw new UserMandatoryFieldException("Login is mandatory");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new UserMandatoryFieldException("Password is mandatory");
        }
    }

}
