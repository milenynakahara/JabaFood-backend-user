package app.jaba.mappers;

import app.jaba.dtos.UserDto;
import app.jaba.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, RoleMapperImpl.class})
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    UserEntity map(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto map(UserEntity userEntity);
}
