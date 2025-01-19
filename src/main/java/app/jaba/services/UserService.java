package app.jaba.services;

import app.jaba.dtos.UpdatePasswordDto;
import app.jaba.dtos.UserDto;
import app.jaba.entities.UpdatePasswordEntity;
import app.jaba.entities.UserEntity;
import app.jaba.exceptions.*;
import app.jaba.mappers.UserMapper;
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
    UserMapper userMapper;

    /**
     * Finds all users with pagination.
     *
     * @param page the page number to retrieve.
     * @param size the number of items per page.
     * @return a list of UserDto objects.
     */
    public List<UserDto> findAll(int page, int size) {
        log.info("Starting findAll with page: {}, size: {}", page, size);

        pageAndSizeValidation.validate(page, size);
        int offset = page > 0 ? (page - 1) * size : 0;
        List<UserDto> users = userRepository.findAll(size, offset).stream().map(userMapper::map).toList();

        log.info("Finished findAll with {} users", users.size());
        return users;
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the UUID of the user.
     * @return a UserDto object.
     * @throws InvalidSizeValueException if the ID is null.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    public UserDto findById(UUID id) {
        log.info("Starting findById with id: {}", id);

        if (id == null) {
            log.error("InvalidSizeValueException: Id is null");
            throw new InvalidSizeValueException("Id is not found");
        }

        String msgError = String.format("User with id %s does not exist.", id);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(msgError));

        log.info("Finished findById with user: {}", user);
        return userMapper.map(user);
    }

    /**
     * Saves a new user.
     *
     * @param userDto the UserDto object to save.
     * @return the saved UserDto object.
     * @throws SaveUserException if there is an error while saving the user.
     */
    public UserDto save(UserDto userDto) {
        log.info("Starting save with userDto: {}", userDto);

        UserEntity userEntity = userMapper.map(userDto);
        validations.forEach(validation -> validation.validate(userEntity));

        var user = userRepository.save(userEntity)
                .orElseThrow(() -> new SaveUserException("Error saving user"));

        saveAddress(user);

        log.info("Finished save with user: {}", user);
        return userMapper.map(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id the UUID of the user.
     * @param userDto the UserDto object with the updated data.
     * @return the updated UserDto object.
     * @throws InvalidSizeValueException if the ID is null.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     * @throws UpdateUserException if there is an error while updating the user.
     */
    public UserDto update(UUID id, UserDto userDto) {
        log.info("Starting update with id: {}, userDto: {}", id, userDto);

        if (id == null) {
            log.error("InvalidSizeValueException: Id is null");
            throw new InvalidSizeValueException("Id is not found");
        }

        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserEntity userEntity = userMapper.map(userDto);
        userEntity.setId(id);
        userEntity.setPassword(userFound.getPassword());
        updateUserValidations.forEach(validation -> validation.validate(userEntity));

        var user = userRepository.update(userEntity)
                .orElseThrow(() -> new UpdateUserException("Error updating user"));

        updateAddress(user);

        log.info("Finished update for user: {}", user.getId());
        return userMapper.map(user);
    }

    /**
     * Updates the password of an existing user.
     *
     * @param id                 the UUID of the user.
     * @param updatePasswordDto  the UpdatePasswordDto object with the updated password.
     * @return the updated UserDto object.
     * @throws InvalidSizeValueException if the ID is null.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     * @throws InvalidPasswordException if the old password is incorrect.
     * @throws UpdatePasswordException if there is an error while updating the password.
     */
    public UserDto updatePassword(UUID id, UpdatePasswordDto updatePasswordDto) {
        log.info("Starting update with id: {}, updatePasswordDto: {}", id, updatePasswordDto);

        UpdatePasswordEntity updatePasswordEntity = userMapper.map(updatePasswordDto);

        updatePasswordValidations.forEach(validation -> validation.validate(updatePasswordEntity));

        if (id == null) {
            log.error("InvalidSizeValueException: Id is null");
            throw new InvalidSizeValueException("Id is not found");
        }

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getPassword().equals(updatePasswordEntity.getOldPassword())) {
            String msgError = "The old password is invalid";
            log.error("InvalidPasswordException: {}", msgError);
            throw new InvalidPasswordException(msgError);
        }

        user.setPassword(updatePasswordEntity.getNewPassword());
        var updateUser = userRepository.updatePassword(user)
                .orElseThrow(() -> new UpdatePasswordException("Error updating password"));

        log.info("Finished update for password: {}", user.getId());
        return userMapper.map(updateUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the UUID of the user.
     * @throws InvalidSizeValueException if the ID is null.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     */
    public void deleteById(UUID id) {
        log.info("Starting delete with id: {}", id);
        if (id == null) {
            log.error("InvalidSizeValueException: Id is null");
            throw new InvalidSizeValueException("Id is not found");
        }

        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        log.info("Finished delete with id: {}", userFound.getId());
        userRepository.deleteById(userFound.getId());
    }

    /**
     * Saves the address of a user.
     *
     * @param userSaved the UserEntity object with the user information.
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
     * @param userUpdated the UserEntity object with the updated user information.
     */
    private void updateAddress(UserEntity userUpdated) {
        userUpdated.setAddress(addressService.update(userUpdated.getId(), userUpdated.getAddress()));
    }
}
