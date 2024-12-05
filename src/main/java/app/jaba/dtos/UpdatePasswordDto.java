package app.jaba.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data Transfer Object for Update Password")
public record UpdatePasswordDto(
        @NonNull
        @Schema(description = "Old password", example = "oldPassword123")
        String oldPassword,

        @NonNull
        @Schema(description = "New password", example = "newPassword123")
        String newPassword,

        @NonNull
        @Schema(description = "Repeat new password", example = "newPassword123")
        String repeatNewPassword
) {
}
