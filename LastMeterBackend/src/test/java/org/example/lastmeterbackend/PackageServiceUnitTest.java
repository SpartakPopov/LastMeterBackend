package org.example.lastmeterbackend;

import org.example.lastmeterbackend.business.serviceImplementations.PackageServiceImpl;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.repositories.PackageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageServiceUnitTest {

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageServiceImpl packageService;

    @Test
    void createPackageReturnsSavedPackage() {
        Package pkg = Package.builder()
                .trackingNumber("TRACK-001")
                .status(PackageStatus.PENDING)
                .build();

        when(packageRepository.save(pkg)).thenReturn(pkg);

        Package result = packageService.createPackage(pkg);

        assertSame(pkg, result);
        verify(packageRepository).save(pkg);
    }

    @Test
    void getByTrackingNumberReturnsPackageWhenItExists() {
        String trackingNumber = "TRACK-002";
        Package pkg = Package.builder()
                .trackingNumber(trackingNumber)
                .status(PackageStatus.DELIVERED_TO_LOCKER)
                .build();

        when(packageRepository.findByTrackingNumber(trackingNumber)).thenReturn(Optional.of(pkg));

        Package result = packageService.getByTrackingNumber(trackingNumber);

        assertSame(pkg, result);
        verify(packageRepository).findByTrackingNumber(trackingNumber);
    }

    @Test
    void updateStatusReturnsUpdatedPackage() {
        String trackingNumber = "TRACK-003";
        PackageStatus status = PackageStatus.PICKED_UP;
        Package updatedPackage = Package.builder()
                .trackingNumber(trackingNumber)
                .status(status)
                .build();

        when(packageRepository.update(trackingNumber, status)).thenReturn(updatedPackage);

        Package result = packageService.updateStatus(trackingNumber, status);

        assertSame(updatedPackage, result);
        verify(packageRepository).update(trackingNumber, status);
    }

    @Test
    void getAllPackagesReturnsAllPackages() {
        List<Package> packages = List.of(
                Package.builder().trackingNumber("TRACK-004").status(PackageStatus.PENDING).build(),
                Package.builder().trackingNumber("TRACK-005").status(PackageStatus.DELIVERED_TO_LOCKER).build()
        );

        when(packageRepository.findAll()).thenReturn(packages);

        List<Package> result = packageService.getAllPackages();

        assertSame(packages, result);
        verify(packageRepository).findAll();
    }

    @Test
    void getAllPackagesByReceiverReturnsReceiverPackages() {
        Long receiverId = 10L;
        List<Package> packages = List.of(
                Package.builder().trackingNumber("TRACK-006").status(PackageStatus.PENDING).build(),
                Package.builder().trackingNumber("TRACK-007").status(PackageStatus.PICKED_UP).build()
        );

        when(packageRepository.findByReceiver(receiverId)).thenReturn(packages);

        List<Package> result = packageService.getAllPackagesByReceiver(receiverId);

        assertSame(packages, result);
        verify(packageRepository).findByReceiver(receiverId);
    }

    @Test
    void getByTrackingNumberThrowsWhenPackageDoesNotExist() {
        String trackingNumber = "MISSING-001";

        when(packageRepository.findByTrackingNumber(trackingNumber)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> packageService.getByTrackingNumber(trackingNumber)
        );

        assertEquals("Package not found with tracking number: " + trackingNumber, exception.getMessage());
        verify(packageRepository).findByTrackingNumber(trackingNumber);
    }
}
