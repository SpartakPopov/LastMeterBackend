package org.example.lastmeterbackend.business.services;

import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Package;

import java.math.BigDecimal;
import java.util.List;

public interface PackageService {
    Package createPackage(String trackingNumber, String description, BigDecimal length, BigDecimal width, BigDecimal height, PackageStatus status, Long receiverId);
    Package updatePackage(Long id, String trackingNumber, String description, BigDecimal length, BigDecimal width, BigDecimal height, PackageStatus status, Long lockerId);
    Package updateStatus(String trackingNumber, PackageStatus status);
    Package getByTrackingNumber(String trackingNumber);
    List<Package> getAllPackages();
    List<Package> getAllPackagesByReceiver(Long receiverId);
    List<Package> getUnassignedPackages();
    Package pickup(String trackingNumber);
}
