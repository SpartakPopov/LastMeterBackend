package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.PackageEntity;
import org.example.lastmeterbackend.DAL.mappers.PackagePersistenceMapper;
import org.example.lastmeterbackend.domain.repositories.PackageRepository;
import org.springframework.stereotype.Repository;
import org.example.lastmeterbackend.domain.models.Package;

import java.util.List;
import java.util.Optional;

@Repository
public class PackageRepositoryImpl implements PackageRepository {

    private final PackageJpaRepository packageJpaRepository;
    private final PackagePersistenceMapper packagePersistenceMapper;

    public PackageRepositoryImpl(PackageJpaRepository packageJpaRepository,
                                 PackagePersistenceMapper packagePersistenceMapper) {
        this.packageJpaRepository = packageJpaRepository;
        this.packagePersistenceMapper = packagePersistenceMapper;
    }

    @Override
    public Package save(Package pkg) {
        PackageEntity entity = packagePersistenceMapper.toEntity(pkg);
        PackageEntity savedEntity = packageJpaRepository.save(entity);
        return packagePersistenceMapper.toDomain(savedEntity);
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
    public void deleteById(Long id) {
        packageJpaRepository.deleteById(id);
    }
}