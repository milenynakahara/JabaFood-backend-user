package app.jaba.repositories;

import app.jaba.entities.UserRoleEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRoleRepository {
    Set<UserRoleEntity> findByUserId(UUID userId);

    Optional<UserRoleEntity> save(UserRoleEntity entity);

    void deleteByUserId(UUID userId);
}
