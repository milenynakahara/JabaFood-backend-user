package app.jaba.repositories;

import app.jaba.entities.RoleEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {

    Optional<RoleEntity> findById(UUID id);

    List<RoleEntity> findAll();

    Optional<RoleEntity> findByName(String name);

}
