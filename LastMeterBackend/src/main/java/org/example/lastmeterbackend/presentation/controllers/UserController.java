package org.example.lastmeterbackend.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "User search and authentication")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Search users by name", description = "Full-text search over first and last name. Used in receiver/requester picker dropdowns.")
    @ApiResponse(responseCode = "200", description = "List of matching users")
    @GetMapping("/search")
    public List<UserResponseDto> searchUsers(
            @Parameter(description = "Search query (partial first or last name)") @RequestParam String q) {
        return userService.searchByName(q).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Login by email", description = "Looks up a user by their email address. Returns the user profile including role. Used as the login mechanism.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found — login successful"),
        @ApiResponse(responseCode = "404", description = "No user with that email address")
    })
    @GetMapping("/login")
    public ResponseEntity<UserResponseDto> loginByEmail(
            @Parameter(description = "Email address of the user (case-insensitive)") @RequestParam String email) {
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
