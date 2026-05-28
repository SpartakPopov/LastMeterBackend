package org.example.lastmeterbackend.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Package;


public interface PackageRepository {
    Package save(Package pkg);
    Package update(String trackingNumber, PackageStatus status);
    Package updateFields(Long id, Package fields);
    Optional<Package> findById(Long id);
    Optional<Package> findByTrackingNumber(String trackingNumber);
    List<Package> findAll();
    List<Package> findByReceiver(Long receiverId);
    List<Package> findUnassigned();
    void deleteById(Long id);

    Package pickup(String trackingNumber);
}
