package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.PackageService;
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

    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public Package createPackage(Package pkg) {
        return packageRepository.save(pkg);
    }


    @Override
    public Package updateStatus(String trackingNumber, PackageStatus status) {
        return packageRepository.update(trackingNumber, status);
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
        return packageRepository.updateFields(id, fields);
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