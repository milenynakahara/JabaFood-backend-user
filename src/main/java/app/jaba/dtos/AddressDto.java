package app.jaba.dtos;

import java.io.Serializable;
import java.util.UUID;

public record AddressDto(UUID id, String street, String city, String state, String zip,
                         String number) implements Serializable {
}
