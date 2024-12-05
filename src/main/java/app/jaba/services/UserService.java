package app.jaba.services;

import app.jaba.entities.UserEntity;
import app.jaba.entities.UserRoleEntity;
import app.jaba.exceptions.SaveUserException;
import app.jaba.exceptions.UpdateUserException;
import app.jaba.repositories.UserRepository;
import app.jaba.services.validations.CreateUserValidation;
import app.jaba.services.validations.PageAndSizeValidation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class UserService {
    UserRepository userRepository;
    AddressService addressService;
    UserRoleService userRoleService;
    List<CreateUserValidation> validations;
    PageAndSizeValidation pageAndSizeValidation;

    public List<UserEntity> findAll(int page, int size) {
        pageAndSizeValidation.validate(page, size);
        int offset = (page - 1) * size;
        return userRepository.findAll(size, offset);
    }

    public UserEntity save(UserEntity userEntity) {
        validations.forEach(validation -> validation.validate(userEntity));

        var userSaved = userRepository.save(userEntity)
                .orElseThrow(() -> new SaveUserException("Error saving user"));

        saveAddress(userSaved);
        saveRoles(userSaved);

        return userSaved;
    }

    private void saveRoles(UserEntity userSaved) {
        if (!CollectionUtils.isEmpty(userSaved.getRoles())) {
            userSaved.getRoles()
                    .forEach(role -> {
                        var userRole = UserRoleEntity.builder()
                                .userId(userSaved.getId())
                                .roleId(role.getId())
                                .build();
                        userRoleService.save(userRole);
                    });
        }
    }

    private void saveAddress(UserEntity userSaved) {
        var address = userSaved.getAddress();
        if (address != null) {
            address.setUserId(userSaved.getId());
            userSaved.setAddress(addressService.save(address));
        }
    }

    private void updateAddress(UserEntity userUpdated) {
        var address = userUpdated.getAddress();
        if (address != null) {
            address.setUserId(userUpdated.getId());
            userUpdated.setAddress(addressService.update(address));
        } else {
            addressService.deleteByUserId(userUpdated.getId());
        }
    }

    private void updateRoles(UserEntity userUpdated) {
        userRoleService.update(userUpdated.getId(), userUpdated.getRoles());
    }

    public UserEntity update(UserEntity userEntity) {
        validations.forEach(validation -> validation.validate(userEntity));

        var userUpdated = userRepository.update(userEntity)
                .orElseThrow(() -> new UpdateUserException("Error updating user"));

        updateAddress(userUpdated);
        updateRoles(userUpdated);

        return userUpdated;
    }
}
