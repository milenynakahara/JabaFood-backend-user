package app.jaba.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressEntity {

    UUID id;
    String street;
    String city;
    String state;
    String zip;
    String number;
    UUID userId;

}
