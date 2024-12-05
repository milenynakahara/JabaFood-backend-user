package app.jaba.services;

import app.jaba.entities.RoleEntity;
import app.jaba.exceptions.RoleNotFoundException;
import app.jaba.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleService {
    RoleRepository repository;

    public RoleEntity getByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }

}
