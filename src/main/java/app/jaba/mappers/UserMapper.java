package app.jaba.mappers;

import app.jaba.dtos.UpdatePasswordDto;
import app.jaba.dtos.UserDto;
import app.jaba.entities.UpdatePasswordEntity;
import app.jaba.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    UserEntity map(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto map(UserEntity userEntity);

    UpdatePasswordEntity map(UpdatePasswordDto updatePasswordDto);

    @Mapping(target = "address", source = "rs")
    default UserEntity map(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("user_id", UUID.class));
        user.setName(rs.getString("user_name"));
        user.setLogin(rs.getString("user_login"));
        user.setEmail(rs.getString("user_email"));
        user.setPassword(rs.getString("user_password"));
        user.setLastUpdate(rs.getObject("user_last_update", LocalDateTime.class));
        return user;
    }
}
