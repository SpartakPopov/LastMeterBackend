package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.DAL.entities.NotificationEntity;
import org.example.lastmeterbackend.DAL.entities.PackageEntity;
import org.example.lastmeterbackend.DAL.entities.UserEntity;
import org.example.lastmeterbackend.DAL.repositories.NotificationJpaRepository;
import org.example.lastmeterbackend.DAL.repositories.PackageJpaRepository;
import org.example.lastmeterbackend.DAL.repositories.UserJpaRepository;
import org.example.lastmeterbackend.business.services.NotificationService;
import org.example.lastmeterbackend.domain.models.Locker;
import org.example.lastmeterbackend.domain.models.Package;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String PACKAGE_DELIVERED_TYPE = "PACKAGE_DELIVERED";

    private final NotificationJpaRepository notificationJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final PackageJpaRepository packageJpaRepository;

    public NotificationServiceImpl(NotificationJpaRepository notificationJpaRepository,
                                   UserJpaRepository userJpaRepository,
                                   PackageJpaRepository packageJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.packageJpaRepository = packageJpaRepository;
    }

    @Override
    public void createPackageDeliveredNotification(Package pkg) {
        if (pkg == null || pkg.getId() == null || pkg.getReceiver() == null || pkg.getReceiver().getId() == null) {
            return;
        }
        if (notificationJpaRepository.existsByPackageEntityIdAndType(pkg.getId(), PACKAGE_DELIVERED_TYPE)) {
            return;
        }

        String lockerNumber = getLockerNumber(pkg);
        String location = getLockerLocation(pkg);
        String pickupInstructions = "Go to " + location + ", open locker " + lockerNumber
                + ", and pick up package " + pkg.getTrackingNumber() + ".";
        UserEntity receiver = userJpaRepository.getReferenceById(pkg.getReceiver().getId());
        PackageEntity packageEntity = packageJpaRepository.getReferenceById(pkg.getId());

        NotificationEntity notification = NotificationEntity.builder()
                .user(receiver)
                .packageEntity(packageEntity)
                .title("Your package is ready for pickup")
                .message("Package " + pkg.getTrackingNumber() + " was delivered to " + lockerNumber
                        + " at " + location + ".")
                .type(PACKAGE_DELIVERED_TYPE)
                .packageDetailsUrl("/packages/" + pkg.getTrackingNumber())
                .pickupInstructions(pickupInstructions)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationJpaRepository.save(notification);
    }

    @Override
    public List<NotificationEntity> getNotificationsForUser(Long userId) {
        return notificationJpaRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<NotificationEntity> getUnreadNotificationsForUser(Long userId) {
        return notificationJpaRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    public NotificationEntity markAsRead(Long notificationId) {
        NotificationEntity notification = notificationJpaRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        notification.setRead(true);
        return notificationJpaRepository.save(notification);
    }

    private String getLockerNumber(Package pkg) {
        Locker locker = pkg.getLocker();
        return locker != null && locker.getLockerNumber() != null ? locker.getLockerNumber() : "the assigned locker";
    }

    private String getLockerLocation(Package pkg) {
        Locker locker = pkg.getLocker();
        if (locker == null || locker.getBuilding() == null) {
            return "the delivery area";
        }
        String buildingName = locker.getBuilding().getName();
        String buildingAddress = locker.getBuilding().getAddress();
        if (buildingName != null && buildingAddress != null) {
            return buildingName + ", " + buildingAddress;
        }
        if (buildingName != null) {
            return buildingName;
        }
        if (buildingAddress != null) {
            return buildingAddress;
        }
        return "the delivery area";
    }
}
