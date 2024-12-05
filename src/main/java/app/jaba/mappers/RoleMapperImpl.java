package app.jaba.mappers;

import app.jaba.dtos.RoleDto;
import app.jaba.entities.RoleEntity;
import app.jaba.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RoleMapperImpl {

    RoleService roleService;

    public RoleEntity map(RoleDto roleDto) {
        return roleService.getByName(roleDto.name());
    }

    public RoleDto map(RoleEntity roleEntity) {
        return RoleDto.valueOf(roleEntity.getName());
    }
}