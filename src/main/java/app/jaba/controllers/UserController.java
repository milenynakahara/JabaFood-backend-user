package app.jaba.controllers;

import app.jaba.dtos.UpdatePasswordDto;
import app.jaba.dtos.UserDto;
import app.jaba.exceptions.*;
import app.jaba.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
@Tag(name = "User", description = "User API")
public class UserController {

    UserService userService;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(@RequestParam(value = "size", defaultValue = "10") int size,
                                                 @RequestParam(value = "page", defaultValue = "0") int page) {
        log.info("Finding all users");
        return ResponseEntity.ok(userService.findAll(page, size));
    }

    @Operation(summary = "Get user")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Object> findById(@PathVariable(value = "id") UUID id) {
        try {
            log.info("Starting search for user with id: {}", id);
            UserDto result = userService.findById(id);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (UserNotFoundException | InvalidSizeValueException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody UserDto userDto) {
        log.info("Starting creation of user: {}", userDto);

        try {
            UserDto result = userService.save(userDto);

            log.info("Starting creation of user with id: {}", result.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (SaveUserException | EmailFormatException | UserMandatoryFieldException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EmailAlreadyInUseException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @Operation(summary = "Update a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") UUID id, @RequestBody UserDto userDto) {
        log.info("Updating user with id: {}", id);
        try {
            UserDto result = userService.update(id, userDto);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UpdateUserException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable("id") UUID id, @Validated @RequestBody UpdatePasswordDto updatePasswordDto) {
        log.info("Updating user password with id: {}", id);
        try {
            UserDto result = userService.updatePassword(id, updatePasswordDto);
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException | InvalidPasswordException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UpdatePasswordException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") UUID id) {
        log.info("Deleting user with id: {}", id);
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch (UserNotFoundException | InvalidSizeValueException e) {
            String msgError = String.format("%s, id: %s", e.getMessage(), id);
            log.error(msgError);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msgError);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

