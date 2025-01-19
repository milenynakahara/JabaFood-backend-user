package app.jaba.controllers;

import app.jaba.dtos.UpdatePasswordDto;
import app.jaba.dtos.UserDto;
import app.jaba.mappers.UserMapper;
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
    UserMapper userMapper;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<UserDto> create(@Validated @RequestBody UserDto userDto) {
        log.info("Creating user: {}", userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.map(userService.save(userMapper.map(userDto))));
    }

    @Operation(summary = "Get a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.map(userService.findById(id)));
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(@RequestParam(value = "size", defaultValue = "10") int size, @RequestParam(value = "page", defaultValue = "0") int page) {
        log.info("Finding all users");
        return ResponseEntity.ok(userService.findAll(page, size)
                .stream()
                .map(userMapper::map)
                .toList());
    }

    @Operation(summary = "Update a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") UUID id, @RequestBody UserDto userDto) {
        log.info("Updating user with id: {}", id);
        return ResponseEntity.ok(userMapper.map(userService.update(id, userMapper.map(userDto))));
    }

    @Operation(summary = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<UserDto> updatePassword(@PathVariable("id") UUID id, @Validated @RequestBody UpdatePasswordDto updatePasswordDto) {
        log.info("Updating user password with id: {}", id);
        return ResponseEntity.ok(userMapper.map(userService.updatePassword(id, userMapper.map(updatePasswordDto))));
    }

    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
