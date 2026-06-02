package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.business.services.UserService;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.presentation.dtos.UserResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public List<UserResponseDto> searchUsers(@RequestParam String q) {
        return userService.searchByName(q).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponseDto> loginByEmail(@RequestParam String email) {
        return userService.findByEmail(email.trim().toLowerCase())
                .map(user -> ResponseEntity.ok(toDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }
}
