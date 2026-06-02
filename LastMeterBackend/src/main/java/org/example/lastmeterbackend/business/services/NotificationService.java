package org.example.lastmeterbackend.business.services;

import org.example.lastmeterbackend.domain.models.Notification;
import org.example.lastmeterbackend.domain.models.Package;

import java.util.List;

public interface NotificationService {
    void createPackageDeliveredNotification(Package pkg);
    List<Notification> getNotificationsForUser(Long userId);
    List<Notification> getUnreadNotificationsForUser(Long userId);
    Notification markAsRead(Long notificationId);
}
