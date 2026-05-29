package org.example.lastmeterbackend.business.services;

import org.example.lastmeterbackend.DAL.entities.NotificationEntity;
import org.example.lastmeterbackend.domain.models.Package;

import java.util.List;

public interface NotificationService {
    void createPackageDeliveredNotification(Package pkg);
    List<NotificationEntity> getNotificationsForUser(Long userId);
    List<NotificationEntity> getUnreadNotificationsForUser(Long userId);
    NotificationEntity markAsRead(Long notificationId);
}
