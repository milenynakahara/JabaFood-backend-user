package app.jaba.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object for Address")
public record AddressDto(
        @Schema(description = "Unique identifier of the address", example = "123e4567-e89b-12d3-a456-426614174000", readOnly = true)
        UUID id,

        @NonNull
        @Schema(description = "Street name", example = "Rua das Flores")
        String street,

        @NonNull
        @Schema(description = "City name", example = "Rio de Janeiro")
        String city,

        @NonNull
        @Schema(description = "State name", example = "RJ")
        String state,

        @NonNull
        @Schema(description = "ZIP code", example = "50712-432")
        String zip,

        @NonNull
        @Schema(description = "House number", example = "13")
        String number
) implements Serializable {
}