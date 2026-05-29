package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.business.services.NotificationService;
import org.example.lastmeterbackend.presentation.dtos.NotificationResponseDto;
import org.example.lastmeterbackend.presentation.mappers.NotificationDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationDtoMapper notificationDtoMapper;

    public NotificationController(NotificationService notificationService,
                                  NotificationDtoMapper notificationDtoMapper) {
        this.notificationService = notificationService;
        this.notificationDtoMapper = notificationDtoMapper;
    }

    @GetMapping("/user/{userId}")
    public List<NotificationResponseDto> getNotificationsForUser(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId).stream()
                .map(notificationDtoMapper::toDto)
                .toList();
    }

    @GetMapping("/user/{userId}/unread")
    public List<NotificationResponseDto> getUnreadNotificationsForUser(@PathVariable Long userId) {
        return notificationService.getUnreadNotificationsForUser(userId).stream()
                .map(notificationDtoMapper::toDto)
                .toList();
    }

    @PatchMapping("/{id}/read")
    public NotificationResponseDto markAsRead(@PathVariable Long id) {
        return notificationDtoMapper.toDto(notificationService.markAsRead(id));
    }
}
