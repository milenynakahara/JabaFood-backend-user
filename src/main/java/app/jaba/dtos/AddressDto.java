package app.jaba.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.UUID;

@Schema(description = "Data Transfer Object for Address")
public record AddressDto(
        @Schema(description = "Unique identifier of the address", example = "123e4567-e89b-12d3-a456-426614174000", readOnly = true)
        UUID id,

        @Schema(description = "Street name", example = "Rua das Flores")
        String street,

        @Schema(description = "City name", example = "Rio de Janeiro")
        String city,

        @Schema(description = "State name", example = "RJ")
        String state,

        @Schema(description = "ZIP code", example = "50712-432")
        String zip,

        @Schema(description = "House number", example = "13")
        String number
) implements Serializable {
}