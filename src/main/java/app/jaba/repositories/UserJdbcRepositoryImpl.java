package app.jaba.repositories;

import app.jaba.entities.AddressEntity;
import app.jaba.entities.RoleEntity;
import app.jaba.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
        String sql = """
                    SELECT u.id AS user_id, u.name AS user_name, u.login AS user_login, u.email AS user_email, u.password AS user_password, u.last_update AS user_last_update,
                           a.id AS address_id, a.street AS address_street, a.city AS address_city, a.state AS address_state, a.zip AS address_zip, a.number AS address_number,
                           STRING_AGG(r.id::text, ',') AS role_ids, STRING_AGG(r.name, ',') AS role_names
                    FROM users u
                    LEFT JOIN addresses a ON u.id = a.user_id
                    LEFT JOIN users_roles ur ON u.id = ur.user_id
                    LEFT JOIN roles r ON ur.role_id = r.id
                    GROUP BY u.id, a.id
                    LIMIT :size OFFSET :offset
                """;

        return jdbcClient.sql(sql)
                .param("size", size)
                .param("offset", offset)
                .query((rs, rowNum) -> {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("user_id", UUID.class));
                    user.setName(rs.getString("user_name"));
                    user.setLogin(rs.getString("user_login"));
                    user.setEmail(rs.getString("user_email"));
                    user.setPassword(rs.getString("user_password"));
                    user.setLastUpdate(rs.getObject("user_last_update", LocalDateTime.class));

                    var addressId = rs.getObject("address_id", UUID.class);
                    if (addressId != null) {
                        AddressEntity address = new AddressEntity();
                        address.setId(addressId);
                        address.setStreet(rs.getString("address_street"));
                        address.setCity(rs.getString("address_city"));
                        address.setState(rs.getString("address_state"));
                        address.setZip(rs.getString("address_zip"));
                        address.setNumber(rs.getString("address_number"));
                        user.setAddress(address);
                    }

                    var roleIds = rs.getString("role_ids");
                    if (StringUtils.hasText(roleIds)) {
                        String[] roleIdsArray = roleIds.split(",");
                        String[] roleNames = rs.getString("role_names").split(",");
                        for (int i = 0; i < roleIdsArray.length; i++) {
                            RoleEntity role = new RoleEntity();
                            role.setId(UUID.fromString(roleIdsArray[i]));
                            role.setName(roleNames[i]);
                            user.getRoles().add(role);
                        }
                    }


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