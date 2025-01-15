package app.jaba.repositories;

import app.jaba.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<UserEntity> {

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> updatePassword(UserEntity userEntity);

    Optional<UserEntity> findById(UUID id);

    void deleteById(UUID id);

}
