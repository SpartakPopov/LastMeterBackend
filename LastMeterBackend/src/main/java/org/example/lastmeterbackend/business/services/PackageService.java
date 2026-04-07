package org.example.lastmeterbackend.business.services;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Package;

import java.util.List;

public interface PackageService {
    //Create an Order using tracking Id
    Package createPackage(Package pkg);
    //Create an order using Qr Code to get the package info
    Package createPackage(String qrCode);
    //Update package status
    Package updateStatus(String trackingNumber, PackageStatus status);
    //Get package by tracking number
    Package getByTrackingNumber(String trackingNumber);
    //Get package by Qr Code
    Package getByQrCode(String qrCode);
    //Get all packages
    List<Package> getAllPackages();
    //Get all packages by receiver
    List<Package> getAllPackagesByReceiver(Long receiverId);
}
