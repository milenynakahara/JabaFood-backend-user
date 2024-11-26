package app.jaba.services;

import app.jaba.entities.UserEntity;
import app.jaba.entities.UserRoleEntity;
import app.jaba.exceptions.SaveAddressException;
import app.jaba.exceptions.SaveUserException;
import app.jaba.exceptions.SaveUserRoleException;
import app.jaba.repositories.AddressRepository;
import app.jaba.repositories.RoleRepository;
import app.jaba.repositories.UserRepository;
import app.jaba.repositories.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class UserService {
    UserRepository userRepository;
    AddressRepository addressRepository;
    UserRoleRepository userRoleRepository;
    RoleRepository roleRepository;

    public UserEntity save(UserEntity userEntity) {
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
        int offset = (page - 1) * size;
        List<UserEntity> userEntities = userRepository.findAll(size, offset);

        userEntities.forEach(userEntity -> {
            loadUserRoles(userEntity);
            loadUserAddress(userEntity);
        });

        return userEntities;
    }

    private void loadUserRoles(UserEntity userEntity) {
        Set<UserRoleEntity> userRoleEntities = userRoleRepository.findByUserId(userEntity.getId());
        for (UserRoleEntity userRoleEntity : userRoleEntities) {
            roleRepository.findById(userRoleEntity.getRoleId())
                    .ifPresent(roleEntity -> userEntity.getRoles().add(roleEntity));
        }

    }

    private void loadUserAddress(UserEntity userEntity) {
        addressRepository.findByUserId(userEntity.getId())
                .ifPresent(userEntity::setAddress);
    }


}
