package app.jaba.repositories;

import app.jaba.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserJdbcRepositoryImpl implements UserRepository {

    JdbcClient jdbcClient;

    @Override
    public Optional<UserEntity> findById(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<UserEntity> findAll(int size, int offset) {
        return jdbcClient.sql("SELECT * FROM users LIMIT :size OFFSET :offset")
                .param("size", size)
                .param("offset", offset)
                .query(UserEntity.class)
                .list();
    }

    @Override
    public Optional<UserEntity> save(UserEntity userEntity) {
        UUID id = UUID.randomUUID();
        LocalDateTime lastUpdate = LocalDateTime.now();
        int result = jdbcClient.sql("""
                        INSERT INTO users (id, name, login, email, password, last_update)
                        VALUES 
                        (:id, :name, :login, :email, :password, :last_update)
                        """)
                .param("id", id)
                .param("name", userEntity.getName())
                .param("login", userEntity.getLogin())
                .param("email", userEntity.getEmail())
                .param("password", userEntity.getPassword())
                .param("last_update", lastUpdate)
                .update();

        if (result != 1)
            return Optional.empty();

        userEntity.setId(id);
        userEntity.setLastUpdate(lastUpdate);
        return Optional.of(userEntity);
    }

    @Override
    public Optional<UserEntity> update(UserEntity userEntity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteById(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        return jdbcClient.sql("SELECT * FROM users WHERE login LIKE :login")
                .param("login", login)
                .query(UserEntity.class)
                .optional();
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return jdbcClient.sql("SELECT * FROM users WHERE email LIKE :email")
                .param("email", email)
                .query(UserEntity.class)
                .optional();
    }
}
