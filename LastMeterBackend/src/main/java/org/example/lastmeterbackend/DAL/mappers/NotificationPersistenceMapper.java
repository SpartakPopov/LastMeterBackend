package org.example.lastmeterbackend.DAL.mappers;

import org.example.lastmeterbackend.DAL.entities.NotificationEntity;
import org.example.lastmeterbackend.domain.models.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationPersistenceMapper {

    public Notification toDomain(NotificationEntity entity) {
        if (entity == null) return null;
        return Notification.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .packageId(entity.getPackageEntity() != null ? entity.getPackageEntity().getId() : null)
                .trackingNumber(entity.getPackageEntity() != null ? entity.getPackageEntity().getTrackingNumber() : null)
                .title(entity.getTitle())
                .message(entity.getMessage())
                .type(entity.getType())
                .packageDetailsUrl(entity.getPackageDetailsUrl())
                .pickupInstructions(entity.getPickupInstructions())
                .read(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
