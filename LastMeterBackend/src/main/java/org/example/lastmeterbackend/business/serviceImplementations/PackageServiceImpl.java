package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.business.services.NotificationService;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.repositories.PackageRepository;
import org.example.lastmeterbackend.exceptions.PackageNotFoundException;
import org.example.lastmeterbackend.exceptions.PackageStateConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Package createPackage(Package pkg) {
        return packageRepository.save(pkg);
    }


    @Override
    public Package updateStatus(String trackingNumber, PackageStatus status) {
        Package existingPackage = getByTrackingNumber(trackingNumber);
        Package updatedPackage = packageRepository.update(trackingNumber, status);

        boolean becameDelivered = existingPackage.getStatus() != PackageStatus.DELIVERED_TO_LOCKER
                && updatedPackage.getStatus() == PackageStatus.DELIVERED_TO_LOCKER;
        if (becameDelivered) {
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
    public Package updatePackage(Long id, Package fields) {
        Package existingPackage = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        Package updatedPackage = packageRepository.updateFields(id, fields);

        if (becameDelivered(existingPackage, updatedPackage)) {
            notificationService.createPackageDeliveredNotification(updatedPackage);
        }

        return updatedPackage;
    }

    private boolean becameDelivered(Package existingPackage, Package updatedPackage) {
        return existingPackage.getStatus() != PackageStatus.DELIVERED_TO_LOCKER
                && updatedPackage.getStatus() == PackageStatus.DELIVERED_TO_LOCKER;
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
}
