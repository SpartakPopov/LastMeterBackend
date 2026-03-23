package org.example.lastmeterbackend.domain.repositories;

import java.util.List;
import java.util.Optional;
import org.example.lastmeterbackend.domain.models.Package;


public interface PackageRepository {
    Package save(Package pkg);
    Optional<Package> findById(Long id);
    Optional<Package> findByTrackingNumber(String trackingNumber);
    List<Package> findAll();
    void deleteById(Long id);
}
