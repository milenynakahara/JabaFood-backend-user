package app.jaba.dtos;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public record UserDto(String id, @NonNull String name, @NonNull String login, @NonNull String email, @NonNull String password, Set<RoleDto> roles,
                      AddressDto address, LocalDateTime lastUpdate) implements Serializable {
}
