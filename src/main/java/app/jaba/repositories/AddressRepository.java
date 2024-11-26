package app.jaba.repositories;

import app.jaba.entities.AddressEntity;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends Repository<AddressEntity> {
    Optional<AddressEntity> findByUserId(UUID userId);
}
