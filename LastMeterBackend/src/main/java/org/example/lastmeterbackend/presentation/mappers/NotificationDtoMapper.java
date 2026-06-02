package org.example.lastmeterbackend.presentation.mappers;

import org.example.lastmeterbackend.domain.models.Notification;
import org.example.lastmeterbackend.presentation.dtos.NotificationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {

    public NotificationResponseDto toDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .packageId(notification.getPackageId())
                .trackingNumber(notification.getTrackingNumber())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .packageDetailsUrl(notification.getPackageDetailsUrl())
                .pickupInstructions(notification.getPickupInstructions())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
