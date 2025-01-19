package app.jaba.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    UUID id;
    String name;
    String login;
    String email;
    String password;
    LocalDateTime lastUpdate;
    AddressEntity address;

}