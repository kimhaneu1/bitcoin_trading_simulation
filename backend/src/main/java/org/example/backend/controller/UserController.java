package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.service.UserService;
import org.example.backend.dto.user.request.UserCreateRequestDto;
import org.example.backend.dto.user.request.UserUpdateRequestDto;
import org.example.backend.dto.user.response.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody @Valid UserCreateRequestDto dto
    ) {
        UserResponseDto response = userService.createUser(dto);
        URI location = URI.create("/api/users/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable String email,
            @RequestBody @Valid UserUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(userService.updateUser(email, dto));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String email
    ) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}