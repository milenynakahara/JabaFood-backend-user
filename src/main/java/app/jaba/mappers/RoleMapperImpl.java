package app.jaba.mappers;

import app.jaba.dtos.RoleDto;
import app.jaba.entities.RoleEntity;
import app.jaba.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RoleMapperImpl {

    RoleRepository roleRepository;

    public RoleEntity map(RoleDto roleDto) {
        return roleRepository.findByName(roleDto.name()).orElse(null);
    }

    public RoleDto map(RoleEntity roleEntity) {
        return RoleDto.valueOf(roleEntity.getName());
    }
}