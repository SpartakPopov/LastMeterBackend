package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.repositories.PackageRepository;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException(
                        "Package not found with tracking number: " + trackingNumber
                ));
    }
    
    @Override
    public Package getByQrCode(String qrCode) {
        return packageRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new RuntimeException(
                        "Package not found with qr code: " + qrCode
                ));
    }
    
     @Override
    List<Package> getAllPackages() {
        return packageRepository.findAll();
    }
    
    @Override List<Package> getAllPackagesByReceiver(Long receiverId) {
        return packageRepository.findByReceiver(receiverId);
    }
    
}