package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.NotificationService;
import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Locker;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.domain.repositories.PackageRepository;
import org.example.lastmeterbackend.exceptions.PackageNotFoundException;
import org.example.lastmeterbackend.exceptions.PackageStateConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final NotificationService notificationService;

    public PackageServiceImpl(PackageRepository packageRepository,
                              NotificationService notificationService) {
        this.packageRepository = packageRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Package createPackage(String trackingNumber, String description, BigDecimal length,
                                 BigDecimal width, BigDecimal height, PackageStatus status, Long receiverId) {
        User receiver = receiverId != null ? User.builder().id(receiverId).build() : null;
        Package pkg = Package.builder()
                .trackingNumber(trackingNumber)
                .description(description)
                .length(length)
                .width(width)
                .height(height)
                .status(status != null ? status : PackageStatus.PENDING)
                .receiver(receiver)
                .build();
        return packageRepository.save(pkg);
    }

    @Override
    public Package updatePackage(Long id, String trackingNumber, String description, BigDecimal length,
                                 BigDecimal width, BigDecimal height, PackageStatus status, Long lockerId) {
        Package existingPackage = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));

        Locker locker = lockerId != null ? Locker.builder().id(lockerId).build() : null;
        Package fields = Package.builder()
                .trackingNumber(trackingNumber)
                .description(description)
                .length(length)
                .width(width)
                .height(height)
                .status(status)
                .locker(locker)
                .build();

        Package updatedPackage = packageRepository.updateFields(id, fields);

        if (becameDelivered(existingPackage, updatedPackage)) {
            notificationService.createPackageDeliveredNotification(updatedPackage);
        }

        return updatedPackage;
    }

    @Override
    public Package updateStatus(String trackingNumber, PackageStatus status) {
        Package existingPackage = getByTrackingNumber(trackingNumber);
        Package updatedPackage = packageRepository.update(trackingNumber, status);

        if (becameDelivered(existingPackage, updatedPackage)) {
            notificationService.createPackageDeliveredNotification(updatedPackage);
        }

        return updatedPackage;
    }

    @Override
    public Package getByTrackingNumber(String trackingNumber) {
        return packageRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new PackageNotFoundException(trackingNumber));
    }

    @Override
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    @Override
    public List<Package> getAllPackagesByReceiver(Long receiverId) {
        return packageRepository.findByReceiver(receiverId);
    }

    @Override
    public List<Package> getUnassignedPackages() {
        return packageRepository.findUnassigned();
    }

    @Override
    @Transactional
    public Package pickup(String trackingNumber) {
        Package pkg = getByTrackingNumber(trackingNumber);
        if (pkg.getStatus() != PackageStatus.DELIVERED_TO_LOCKER) {
            throw new PackageStateConflictException(pkg.getStatus());
        }
        return packageRepository.pickup(trackingNumber);
    }

    private boolean becameDelivered(Package before, Package after) {
        return before.getStatus() != PackageStatus.DELIVERED_TO_LOCKER
                && after.getStatus() == PackageStatus.DELIVERED_TO_LOCKER;
    }
}
