package app.jaba.services.validations.updatepassword;

import app.jaba.entities.UpdatePasswordEntity;
import app.jaba.exceptions.MissingPasswordException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PasswordMandatoryFieldValidation implements UpdatePasswordValidation {
    public void validate(UpdatePasswordEntity updatePasswordEntity) {
        if (!StringUtils.hasText(updatePasswordEntity.getOldPassword())) {
            throw new MissingPasswordException("Old password is mandatory");
        }

        if (!StringUtils.hasText(updatePasswordEntity.getNewPassword())) {
            throw new MissingPasswordException("New password is mandatory");
        }

        if (!StringUtils.hasText(updatePasswordEntity.getRepeatNewPassword())) {
            throw new MissingPasswordException("Repeat new password is mandatory");
        }
    }
}
