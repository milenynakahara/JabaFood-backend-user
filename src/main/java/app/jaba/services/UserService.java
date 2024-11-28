package app.jaba.services;

import app.jaba.entities.UserEntity;
import app.jaba.entities.UserRoleEntity;
import app.jaba.exceptions.SaveAddressException;
import app.jaba.exceptions.SaveUserException;
import app.jaba.exceptions.SaveUserRoleException;
import app.jaba.repositories.AddressRepository;
import app.jaba.repositories.UserRepository;
import app.jaba.repositories.UserRoleRepository;
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
    AddressRepository addressRepository;
    UserRoleRepository userRoleRepository;
    List<CreateUserValidation> validations;
    PageAndSizeValidation pageAndSizeValidation;

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
            userSaved.getRoles().forEach(role -> {
                var userRole = UserRoleEntity.builder()
                        .userId(userSaved.getId())
                        .roleId(role.getId())
                        .build();
                userRoleRepository.save(userRole)
                        .orElseThrow(() -> new SaveUserRoleException("Error saving user role"));
            });
        }
    }

    private void saveAddress(UserEntity userSaved) {
        var address = userSaved.getAddress();
        if (address == null) {
            return;
        }
        address.setUserId(userSaved.getId());
        addressRepository.save(address)
                .orElseThrow(() -> new SaveAddressException("Error saving address"));
    }

    public List<UserEntity> findAll(int page, int size) {
        pageAndSizeValidation.validate(page, size);
        int offset = (page - 1) * size;
        return userRepository.findAll(size, offset);
    }

}
