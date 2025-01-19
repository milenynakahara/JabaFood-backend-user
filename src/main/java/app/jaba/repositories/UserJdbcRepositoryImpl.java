package app.jaba.repositories;

import app.jaba.entities.UserEntity;
import app.jaba.mappers.AddressMapper;
import app.jaba.mappers.UserMapper;
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
    UserMapper userMapper;
    AddressMapper addressMapper;

    @Override
    public Optional<UserEntity> findById(UUID id) {
        String sql = """               
                 SELECT u.id AS user_id, u.name AS user_name, u.login AS user_login, u.email AS user_email, u.password AS user_password, u.last_update AS user_last_update,
                        a.id AS address_id, a.street AS address_street, a.city AS address_city, a.state AS address_state, a.zip AS address_zip, a.number AS address_number                          \s
                 FROM users u
                 LEFT JOIN addresses a ON u.id = a.user_id
                 WHERE u.id = ?
                """;
        return jdbcClient
                .sql(sql)
                .param(id)
                .query(rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    var user = userMapper.map(rs);
                    user.setAddress(addressMapper.map(rs));

                    return Optional.of(user);
                });
    }


    @Override
    public List<UserEntity> findAll(int size, int offset) {
        String sql = """
                    SELECT u.id AS user_id, u.name AS user_name, u.login AS user_login, u.email AS user_email, u.password AS user_password, u.last_update AS user_last_update,
                           a.id AS address_id, a.street AS address_street, a.city AS address_city, a.state AS address_state, a.zip AS address_zip, a.number AS address_number
                    FROM users u
                    LEFT JOIN addresses a ON u.id = a.user_id
                    GROUP BY u.id, a.id
                    LIMIT :size OFFSET :offset
                """;

        return jdbcClient.sql(sql)
                .param("size", size)
                .param("offset", offset)
                .query((rs, rowNum) -> {
                    var user = userMapper.map(rs);
                    user.setAddress(addressMapper.map(rs));
                    return user;
                })
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
        LocalDateTime lastUpdate = LocalDateTime.now();
        int result = this.jdbcClient
                .sql("UPDATE users SET name = :name, login = :login, email = :email, last_update = :last_update WHERE id = :id")
                .param("name", userEntity.getName())
                .param("login", userEntity.getLogin())
                .param("email", userEntity.getEmail())
                .param("last_update", lastUpdate)
                .param("id", userEntity.getId())
                .update();

        if (result != 1)
            return Optional.empty();

        userEntity.setLastUpdate(lastUpdate);
        return Optional.of(userEntity);
    }

    @Override
    public void deleteById(UUID id) {
        jdbcClient.sql("DELETE FROM users WHERE id = :id")
                .param("id", id)
                .update();
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

    @Override
    public Optional<UserEntity> updatePassword(UserEntity userEntity) {
        int result = jdbcClient.sql("UPDATE users SET password = :password WHERE id = :id")
                .param("password", userEntity.getPassword())
                .param("id", userEntity.getId())
                .update();
        if (result == 1)
            return Optional.of(userEntity);
        return Optional.empty();
    }

}
