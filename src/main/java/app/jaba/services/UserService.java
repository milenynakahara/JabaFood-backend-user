package app.jaba.services;

import app.jaba.entities.UpdatePasswordEntity;
import app.jaba.entities.UserEntity;
import app.jaba.exceptions.*;
import app.jaba.repositories.UserRepository;
import app.jaba.services.validations.PageAndSizeValidation;
import app.jaba.services.validations.updatepassword.UpdatePasswordValidation;
import app.jaba.services.validations.user.CreateUserValidation;
import app.jaba.services.validations.user.UpdateUserValidation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    UserRepository userRepository;
    AddressService addressService;
    List<CreateUserValidation> validations;
    List<UpdateUserValidation> updateUserValidations;
    PageAndSizeValidation pageAndSizeValidation;
    List<UpdatePasswordValidation> updatePasswordValidations;

    public List<UserEntity> findAll(int page, int size) {
        pageAndSizeValidation.validate(page, size);
        int offset = page > 0 ? (page - 1) * size : 0;
        return userRepository.findAll(size, offset);
    }

    public UserEntity save(UserEntity userEntity) {
        validations.forEach(validation -> validation.validate(userEntity));

        var userSaved = userRepository.save(userEntity)
                .orElseThrow(() -> new SaveUserException("Error saving user"));

        saveAddress(userSaved);

        return userSaved;
    }

    public UserEntity update(UUID id, UserEntity userEntity) {
        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userEntity.setId(id);
        userEntity.setPassword(userFound.getPassword());
        updateUserValidations.forEach(validation -> validation.validate(userEntity));

        var userUpdated = userRepository.update(userEntity)
                .orElseThrow(() -> new UpdateUserException("Error updating user"));

        updateAddress(userUpdated);

        return userUpdated;
    }

    public UserEntity updatePassword(UUID id, UpdatePasswordEntity updatePasswordEntity) {
        updatePasswordValidations.forEach(validation -> validation.validate(updatePasswordEntity));

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getPassword().equals(updatePasswordEntity.getOldPassword())) {
            throw new InvalidPasswordException("The old password is invalid");
        }

        user.setPassword(updatePasswordEntity.getNewPassword());
        return userRepository.updatePassword(user)
                .orElseThrow(() -> new UpdatePasswordException("Error updating password"));
    }

    private void saveAddress(UserEntity userSaved) {
        var address = userSaved.getAddress();
        if (address != null) {
            address.setUserId(userSaved.getId());
            userSaved.setAddress(addressService.save(address));
        }
    }

    private void updateAddress(UserEntity userUpdated) {
        userUpdated.setAddress(addressService.update(userUpdated.getId(), userUpdated.getAddress()));
    }

    public void deleteById(UUID id) {
        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteById(userFound.getId());
    }

}
