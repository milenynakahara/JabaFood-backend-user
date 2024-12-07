package app.jaba.repositories;

import app.jaba.entities.AddressEntity;
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
public class AddressJdbcRepositoryImpl implements AddressRepository {

    JdbcClient jdbcClient;

    @Override
    public Optional<AddressEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<AddressEntity> findAll(int size, int offset) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<AddressEntity> save(AddressEntity entity) {
        UUID id = UUID.randomUUID();
        int result = jdbcClient.sql("""
                                            INSERT INTO addresses (id, street, city, state, zip, number, user_id)
                                            VALUES 
                                            (:id, :street, :city, :state, :zip, :number, :user_id)
                                            """)
                .param("id", id)
                .param("street", entity.getStreet())
                .param("city", entity.getCity())
                .param("state", entity.getState())
                .param("zip", entity.getZip())
                .param("number", entity.getNumber())
                .param("user_id", entity.getUserId())
                .update();

        if (result != 1)
            return Optional.empty();

        entity.setId(id);
        return Optional.of(entity);
    }

    @Override
    public Optional<AddressEntity> update(AddressEntity entity) {
        int result =
                jdbcClient
                        .sql("""
                            UPDATE addresses 
                            SET 
                                street = :street, 
                                city = :city,
                                state = :state,
                                zip = :zip,
                                number = :number 
                            WHERE id = :id
                        """)
                        .param("street", entity.getStreet())
                        .param("city", entity.getCity())
                        .param("state", entity.getState())
                        .param("zip", entity.getZip())
                        .param("number", entity.getNumber())
                        .param("id", entity.getId())
                        .update();

        if (result != 1)
            return Optional.empty();

        return Optional.of(entity);
    }
    
    @Override
    public void deleteById(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<AddressEntity> findByUserId(UUID userId) {
        return jdbcClient.sql("SELECT * FROM addresses WHERE user_id = :user_id")
                .param("user_id", userId)
                .query(AddressEntity.class)
                .optional();
    }

}
