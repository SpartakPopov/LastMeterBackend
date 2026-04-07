package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageJpaRepository extends JpaRepository<PackageEntity, Long> {
    Optional<PackageEntity> findByTrackingNumber(String trackingNumber);
    List<PackageEntity> findByReceiver(Long receiverId);
    List<PackageEntity> findAll();
    
}