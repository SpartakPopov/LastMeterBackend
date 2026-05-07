package org.example.lastmeterbackend.unitTests;

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
    void createPackage_returnsSavedPackage() {
        // Arrange
        Package newPackage = createPackage("TRACK-001", PackageStatus.PENDING);
        when(packageRepository.save(newPackage)).thenReturn(newPackage);

        // Act
        Package result = packageService.createPackage(newPackage);

        // Assert
        assertEquals(newPackage, result);
        verify(packageRepository).save(newPackage);
    }

    @Test
    void getByTrackingNumber_returnsPackageWhenItExists() {
        // Arrange
        String trackingNumber = "TRACK-002";
        Package expectedPackage = createPackage(trackingNumber, PackageStatus.DELIVERED_TO_LOCKER);
        when(packageRepository.findByTrackingNumber(trackingNumber)).thenReturn(Optional.of(expectedPackage));

        // Act
        Package result = packageService.getByTrackingNumber(trackingNumber);

        // Assert
        assertEquals(expectedPackage, result);
        verify(packageRepository).findByTrackingNumber(trackingNumber);
    }

    @Test
    void updateStatus_returnsUpdatedPackage() {
        // Arrange
        String trackingNumber = "TRACK-003";
        PackageStatus status = PackageStatus.PICKED_UP;
        Package updatedPackage = createPackage(trackingNumber, status);
        when(packageRepository.update(trackingNumber, status)).thenReturn(updatedPackage);

        // Act
        Package result = packageService.updateStatus(trackingNumber, status);

        // Assert
        assertEquals(updatedPackage, result);
        verify(packageRepository).update(trackingNumber, status);
    }

    @Test
    void getAllPackages_returnsAllPackages() {
        // Arrange
        List<Package> expectedPackages = List.of(
                createPackage("TRACK-004", PackageStatus.PENDING),
                createPackage("TRACK-005", PackageStatus.DELIVERED_TO_LOCKER)
        );
        when(packageRepository.findAll()).thenReturn(expectedPackages);

        // Act
        List<Package> result = packageService.getAllPackages();

        // Assert
        assertEquals(expectedPackages, result);
        verify(packageRepository).findAll();
    }

    @Test
    void getAllPackagesByReceiver_returnsReceiverPackages() {
        // Arrange
        Long receiverId = 10L;
        List<Package> expectedPackages = List.of(
                createPackage("TRACK-006", PackageStatus.PENDING),
                createPackage("TRACK-007", PackageStatus.PICKED_UP)
        );
        when(packageRepository.findByReceiver(receiverId)).thenReturn(expectedPackages);

        // Act
        List<Package> result = packageService.getAllPackagesByReceiver(receiverId);

        // Assert
        assertEquals(expectedPackages, result);
        verify(packageRepository).findByReceiver(receiverId);
    }

    @Test
    void getByTrackingNumber_throwsExceptionWhenPackageDoesNotExist() {
        // Arrange
        String trackingNumber = "MISSING-001";
        when(packageRepository.findByTrackingNumber(trackingNumber)).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> packageService.getByTrackingNumber(trackingNumber)
        );

        // Assert
        assertEquals("Package not found with tracking number: " + trackingNumber, exception.getMessage());
        verify(packageRepository).findByTrackingNumber(trackingNumber);
    }

    private Package createPackage(String trackingNumber, PackageStatus status) {
        return Package.builder()
                .trackingNumber(trackingNumber)
                .description(trackingNumber + " description")
                .status(status)
                .build();
    }
}
