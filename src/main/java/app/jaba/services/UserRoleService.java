package app.jaba.services;

import app.jaba.entities.RoleEntity;
import app.jaba.entities.UserRoleEntity;
import app.jaba.exceptions.SaveUserRoleException;
import app.jaba.repositories.UserRoleRepository;
import app.jaba.services.validations.CreateUserRoleValidation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class UserRoleService {

    UserRoleRepository repository;
    List<CreateUserRoleValidation> validations;

    public UserRoleEntity save(UserRoleEntity entity) {
        validations.forEach(validation -> validation.validate(entity));
        return repository.save(entity)
                .orElseThrow(() -> new SaveUserRoleException("Error saving user role"));
    }

    public Set<UserRoleEntity> update(UUID userId, Set<RoleEntity> roles) {
        if(Objects.isNull(userId)) {
            throw new SaveUserRoleException("User id is required");
        }

        repository.deleteByUserId(userId);

        Set<UserRoleEntity> userRoleEntities = new HashSet<>();
        if (!CollectionUtils.isEmpty(roles)) {
            roles.forEach(role -> {
                var userRole = UserRoleEntity.builder()
                        .userId(userId)
                        .roleId(role.getId())
                        .build();
                userRoleEntities.add(save(userRole));
            });
        }

        return userRoleEntities;
    }

}
