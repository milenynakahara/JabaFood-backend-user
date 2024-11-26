package app.jaba.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {
    Optional<T> findById(UUID id);

    List<T> findAll(int size, int offset);

    Optional<T> save(T entity);

    Optional<T> update(T entity);

    void deleteById(UUID id);
}
