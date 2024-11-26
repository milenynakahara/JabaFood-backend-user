package app.jaba.repositories;

import app.jaba.entities.RoleEntity;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RoleJdbcRepositoryImpl implements RoleRepository {

    JdbcClient jdbcClient;

    @Override
    public Optional<RoleEntity> findById(UUID id) {
        return jdbcClient.sql("SELECT * FROM roles WHERE id = :id")
                .param("id", id)
                .query(RoleEntity.class)
                .optional();
    }

    @Override
    public List<RoleEntity> findAll() {
        return jdbcClient.sql("SELECT * FROM roles")
                .query(RoleEntity.class)
                .list();
    }

    @Override
    public Optional<RoleEntity> findByName(String name) {
        return jdbcClient.sql("SELECT * FROM roles WHERE name = :name")
                .param("name", name)
                .query(RoleEntity.class)
                .optional();
    }
}
