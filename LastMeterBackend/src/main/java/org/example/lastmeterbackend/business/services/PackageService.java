package org.example.lastmeterbackend.business.services;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Package;

import java.util.List;

public interface PackageService {
    //Create an Order using tracking Id
    Package createPackage(Package pkg);
    //Update package status
    Package updateStatus(String trackingNumber, PackageStatus status);
    //Get package by tracking number
    Package getByTrackingNumber(String trackingNumber);
    //Get all packages
    List<Package> getAllPackages();
    //Get all packages by receiver
    List<Package> getAllPackagesByReceiver(Long receiverId);
}
