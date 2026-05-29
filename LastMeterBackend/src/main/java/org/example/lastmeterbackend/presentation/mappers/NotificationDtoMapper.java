package org.example.lastmeterbackend.presentation.mappers;

import org.example.lastmeterbackend.DAL.entities.NotificationEntity;
import org.example.lastmeterbackend.presentation.dtos.NotificationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {

    public NotificationResponseDto toDto(NotificationEntity notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .userId(notification.getUser() != null ? notification.getUser().getId() : null)
                .packageId(notification.getPackageEntity() != null ? notification.getPackageEntity().getId() : null)
                .trackingNumber(notification.getPackageEntity() != null
                        ? notification.getPackageEntity().getTrackingNumber()
                        : null)
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
