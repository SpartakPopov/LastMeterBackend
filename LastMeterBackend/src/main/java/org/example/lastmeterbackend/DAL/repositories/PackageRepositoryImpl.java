package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.PackageEntity;
import org.example.lastmeterbackend.DAL.mappers.PackagePersistenceMapper;
import org.example.lastmeterbackend.domain.enums.LockerStatus;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.repositories.PackageRepository;
import org.springframework.stereotype.Repository;
import org.example.lastmeterbackend.domain.models.Package;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PackageRepositoryImpl implements PackageRepository {

    private final PackageJpaRepository packageJpaRepository;
    private final PackagePersistenceMapper packagePersistenceMapper;
    private final LockerJpaRepository lockerJpaRepository;

    public PackageRepositoryImpl(PackageJpaRepository packageJpaRepository,
                                 PackagePersistenceMapper packagePersistenceMapper,
                                 LockerJpaRepository lockerJpaRepository) {
        this.packageJpaRepository = packageJpaRepository;
        this.packagePersistenceMapper = packagePersistenceMapper;
        this.lockerJpaRepository = lockerJpaRepository;
    }

    @Override
    public Package save(Package pkg) {
        PackageEntity entity = packagePersistenceMapper.toEntity(pkg);
        PackageEntity savedEntity = packageJpaRepository.save(entity);
        return packagePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Package update(String trackingNumber, PackageStatus status) {
        PackageEntity entity = packageJpaRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Package not found with tracking number: " + trackingNumber
                ));
        entity.setStatus(status);
        PackageEntity updatedEntity = packageJpaRepository.save(entity);
        return packagePersistenceMapper.toDomain(updatedEntity);
    }

    
    @Override
    public Optional<Package> findById(Long id) {
        return packageJpaRepository.findById(id)
                .map(packagePersistenceMapper::toDomain);
    }


    @Override
    public Optional<Package> findByTrackingNumber(String trackingNumber) {
        return packageJpaRepository.findByTrackingNumber(trackingNumber)
                .map(packagePersistenceMapper::toDomain);
    }

    @Override
    public List<Package> findAll() {
        return packageJpaRepository.findAll()
                .stream()
                .map(packagePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Package> findByReceiver(Long receiverId) {
        return packageJpaRepository.findByReceiverId(receiverId)
                .stream()
                .map(packagePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Package> findUnassigned() {
        return packageJpaRepository.findByReceiverIsNull()
                .stream()
                .map(packagePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Package updateFields(Long id, Package fields) {
        PackageEntity entity = packageJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        if (fields.getTrackingNumber() != null) entity.setTrackingNumber(fields.getTrackingNumber());
        if (fields.getDescription() != null) entity.setDescription(fields.getDescription());
        entity.setLength(fields.getLength());
        entity.setWidth(fields.getWidth());
        entity.setHeight(fields.getHeight());
        return packagePersistenceMapper.toDomain(packageJpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        packageJpaRepository.deleteById(id);
    }

    @Override
    public Package pickup(String trackingNumber) {
        PackageEntity entity = packageJpaRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Package not found with tracking number: " + trackingNumber
                ));
        entity.setStatus(PackageStatus.PICKED_UP);
        entity.setPickedUpAt(LocalDateTime.now());
        if (entity.getLocker() != null) {
            entity.getLocker().setStatus(LockerStatus.AVAILABLE);
            lockerJpaRepository.save(entity.getLocker());
        }
        return packagePersistenceMapper.toDomain(packageJpaRepository.save(entity));
    }
}