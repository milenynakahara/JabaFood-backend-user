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
        pageAndSizeValidation.validate(page, size);
        int offset = page > 0 ? (page - 1) * size : 0;
        return userRepository.findAll(size, offset).stream().map(userMapper::map).toList();
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
        if (id == null) {
            throw new InvalidSizeValueException("Id is not found");
        }
        String msgError = String.format("User with id %s does not exist.", id);

        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(msgError));
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
        UserEntity userEntity = userMapper.map(userDto);
        validations.forEach(validation -> validation.validate(userEntity));

        var user = userRepository.save(userEntity)
                .orElseThrow(() -> new SaveUserException("Error saving user"));

        saveAddress(user);
        return userMapper.map(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id      the UUID of the user.
     * @param userDto the UserDto object with the updated data.
     * @return the updated UserDto object.
     * @throws InvalidSizeValueException if the ID is null.
     * @throws UserNotFoundException if the user with the given ID does not exist.
     * @throws UpdateUserException if there is an error while updating the user.
     */
    public UserDto update(UUID id, UserDto userDto) {
        UserEntity userEntity = userMapper.map(userDto);

        if (id == null) {
            throw new InvalidSizeValueException("Id is not found");
        }
        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userEntity.setId(id);
        userEntity.setPassword(userFound.getPassword());
        updateUserValidations.forEach(validation -> validation.validate(userEntity));

        var user = userRepository.update(userEntity)
                .orElseThrow(() -> new UpdateUserException("Error updating user"));

        updateAddress(user);

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
        UpdatePasswordEntity updatePasswordEntity = userMapper.map(updatePasswordDto);

        updatePasswordValidations.forEach(validation -> validation.validate(updatePasswordEntity));

        if (id == null) {
            throw new InvalidSizeValueException("Id is not found");
        }

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getPassword().equals(updatePasswordEntity.getOldPassword())) {
            throw new InvalidPasswordException("The old password is invalid");
        }

        user.setPassword(updatePasswordEntity.getNewPassword());
        var updateUser = userRepository.updatePassword(user)
                .orElseThrow(() -> new UpdatePasswordException("Error updating password"));

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
        if (id == null) {
            throw new InvalidSizeValueException("Id is not found");
        }

        var userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
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
