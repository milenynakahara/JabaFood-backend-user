package app.jaba.repositories;

import app.jaba.entities.UserRoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserRoleJdbcRepositoryImpl implements UserRoleRepository {

    JdbcClient jdbcClient;

    @Override
    public Set<UserRoleEntity> findByUserId(UUID userId) {
        return jdbcClient.sql("""
                        SELECT * FROM users_roles
                        WHERE user_id = :user_id
                        """)
                .param("user_id", userId)
                .query(UserRoleEntity.class)
                .set();
    }

    @Override
    public Optional<UserRoleEntity> save(UserRoleEntity entity) {
        int result = jdbcClient.sql("""
                        INSERT INTO users_roles (user_id, role_id)
                        VALUES 
                        (:user_id, :role_id)
                        """)
                .param("user_id", entity.getUserId())
                .param("role_id", entity.getRoleId())
                .update();

        if (result != 1)
            return Optional.empty();

        return Optional.of(entity);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jdbcClient.sql("""
                        DELETE FROM user_roles
                        WHERE user_id = :user_id
                        """)
                .param("user_id", userId)
                .update();
    }
}
