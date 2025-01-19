package app.jaba.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object for User")
public record UserDto(
        @Schema(description = "Unique identifier of the user", example = "04870a33-235f-4610-a74e-9e057c46a134", readOnly = true)
        String id,

        @NonNull
        @Schema(description = "Name of the user", example = "Roberto Carlos", required = true)
        String name,

        @NonNull
        @Schema(description = "Login of the user", example = "roberto.carlos", required = true)
        String login,

        @NonNull
        @Schema(description = "Email of the user", example = "roberto.carlos@jaba.com", required = true)
        String email,

        @NonNull
        @Schema(description = "Password of the user", example = "password123", writeOnly = true)
        String password,

        @Schema(description = "Address of the user")
        AddressDto address,

        @Schema(description = "Last update timestamp", example = "2024-11-26T21:22:48.434Z", readOnly = true)
        LocalDateTime lastUpdate
) implements Serializable {
}