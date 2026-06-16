package org.example.lastmeterbackend.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lastmeterbackend.business.services.NotificationService;
import org.example.lastmeterbackend.presentation.dtos.NotificationResponseDto;
import org.example.lastmeterbackend.presentation.mappers.NotificationDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Notifications", description = "Delivery notifications — retrieve and mark alerts for package recipients")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationDtoMapper notificationDtoMapper;

    public NotificationController(NotificationService notificationService,
                                  NotificationDtoMapper notificationDtoMapper) {
        this.notificationService = notificationService;
        this.notificationDtoMapper = notificationDtoMapper;
    }

    @Operation(summary = "Get all notifications for a user", description = "Returns all delivery notifications (read and unread) for the given user, sorted by newest first.")
    @ApiResponse(responseCode = "200", description = "List of notifications")
    @GetMapping("/user/{userId}")
    public List<NotificationResponseDto> getNotificationsForUser(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId).stream()
                .map(notificationDtoMapper::toDto)
                .toList();
    }

    @Operation(summary = "Get unread notifications for a user", description = "Returns only unread notifications. Use this to drive the badge counter in the UI.")
    @ApiResponse(responseCode = "200", description = "List of unread notifications")
    @GetMapping("/user/{userId}/unread")
    public List<NotificationResponseDto> getUnreadNotificationsForUser(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        return notificationService.getUnreadNotificationsForUser(userId).stream()
                .map(notificationDtoMapper::toDto)
                .toList();
    }

    @Operation(summary = "Mark a notification as read", description = "Sets the read flag on a notification. Idempotent — calling it on an already-read notification is safe.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notification marked as read"),
        @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @PatchMapping("/{id}/read")
    public NotificationResponseDto markAsRead(
            @Parameter(description = "ID of the notification") @PathVariable Long id) {
        return notificationDtoMapper.toDto(notificationService.markAsRead(id));
    }
}
