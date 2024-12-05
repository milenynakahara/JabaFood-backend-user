package app.jaba.controllers;

import app.jaba.dtos.UserDto;
import app.jaba.entities.UserEntity;
import app.jaba.mappers.UserMapper;
import app.jaba.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserController {

    UserService userService;
    UserMapper userMapper;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<UserDto> create(@Validated @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userMapper.map(userService.save(userMapper.map(userDto))));
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(@RequestParam(value = "size", defaultValue = "10") int size, @RequestParam(value = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(userService.findAll(page, size)
                                         .stream()
                                         .map(userMapper::map)
                                         .toList());
    }

    @Operation(summary = "Update a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated successfully")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable UUID id, @RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.map(userDto);
        userEntity.setId(id);
        return ResponseEntity.ok(userMapper.map(userService.update(userMapper.map(userDto))));
    }

}
