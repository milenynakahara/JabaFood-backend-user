package app.jaba.services.validations.updatepassword;

import app.jaba.entities.UpdatePasswordEntity;
import app.jaba.exceptions.PasswordMatchException;
import org.springframework.stereotype.Component;

@Component
public class NewPasswordMatchValidation implements UpdatePasswordValidation {
    @Override
    public void validate(UpdatePasswordEntity entity) {
        if (!entity.getNewPassword().equals(entity.getRepeatNewPassword())) {
            throw new PasswordMatchException("New password and repeat new password do not match");
        }
    }
}
