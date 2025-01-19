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

    /**
     * Finds all users with pagination.
     *
     * @param page the page number to retrieve.
     * @param size the number of items per page.
     * @return a list of UserEntity objects.
     */
    public List<UserEntity> findAll(int page, int size) {
        pageAndSizeValidation.validate(page, size);
        int offset = page > 0 ? (page - 1) * size : 0;
        return userRepository.findAll(size, offset);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the UUID of the user.
     * @return a UserEntity object.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    public UserEntity findById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * Saves a new user.
     *
     * @param userEntity the UserEntity object to save.
     * @return the saved UserEntity object.
     * @throws SaveUserException if there is an error while saving the user.
     */
    public UserEntity save(UserEntity userEntity) {
        validations.forEach(validation -> validation.validate(userEntity));

        var userSaved = userRepository.save(userEntity)
                .orElseThrow(() -> new SaveUserException("Error saving user"));

        saveAddress(userSaved);

        return userSaved;
    }

    /**
     * Updates an existing user.
     *
     * @param id      the UUID of the user.
     * @param userEntity the UserEntity object with the updated data.
     * @return the updated UserDto object.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     * @throws UpdateUserException if there is an error while updating the user.
     */
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

    /**
     * Updates the password of an existing user.
     *
     * @param id                 the UUID of the user.
     * @param updatePasswordEntity  the UpdatePasswordEntity object with the updated password.
     * @return the updated UserEntity object.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     * @throws InvalidPasswordException if the old password is incorrect.
     * @throws UpdatePasswordException if there is an error while updating the password.
     */
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

    /**
     * Deletes a user by their ID.
     *
     * @param id the UUID of the user.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    public void deleteById(UUID id) {
        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteById(userFound.getId());
    }

    /**
     * Saves the address of a user.
     *
     * @param userSaved the UserEntity object with the user address information.
     */
    private void saveAddress(UserEntity userSaved) {
        var address = userSaved.getAddress();
        if (address != null) {
            address.setUserId(userSaved.getId());
            userSaved.setAddress(addressService.save(address));
        }
    }

    /**
     * Updates the address of a user.
     *
     * @param userUpdated the UserEntity object with the updated user address information.
     */
    private void updateAddress(UserEntity userUpdated) {
        userUpdated.setAddress(addressService.update(userUpdated.getId(), userUpdated.getAddress()));
    }

}
