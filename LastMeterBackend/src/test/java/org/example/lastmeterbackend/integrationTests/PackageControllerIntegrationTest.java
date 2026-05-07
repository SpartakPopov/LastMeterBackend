package org.example.lastmeterbackend.integrationTests;

import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.enums.LockerSize;
import org.example.lastmeterbackend.domain.enums.LockerStatus;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.enums.UserRole;
import org.example.lastmeterbackend.domain.models.Building;
import org.example.lastmeterbackend.domain.models.Locker;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.presentation.controllers.PackageController;
import org.example.lastmeterbackend.presentation.mappers.PackageDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PackageController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(PackageDtoMapper.class)
class PackageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PackageService packageService;

    @Test
    void createPackage_returnsCreatedPackage() throws Exception {
        // Arrange
        Package createdPackage = createPackage(1L, "TRACK-100", PackageStatus.PENDING);
        when(packageService.createPackage(any(Package.class))).thenReturn(createdPackage);

        // Act + Assert
        mockMvc.perform(post("/packages/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "trackingNumber": "TRACK-100",
                                  "description": "Laptop",
                                  "length": 30.50,
                                  "width": 20.50,
                                  "height": 10.00,
                                  "status": "PENDING"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.trackingNumber").value("TRACK-100"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.receiverId").value(11))
                .andExpect(jsonPath("$.lockerNumber").value("L-01"))
                .andExpect(jsonPath("$.buildingName").value("Main Building"));

        verify(packageService).createPackage(any(Package.class));
    }

    @Test
    void getByTrackingNumber_returnsPackage() throws Exception {
        // Arrange
        Package pkg = createPackage(2L, "TRACK-200", PackageStatus.DELIVERED_TO_LOCKER);
        when(packageService.getByTrackingNumber("TRACK-200")).thenReturn(pkg);

        // Act + Assert
        mockMvc.perform(get("/packages/TRACK-200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.trackingNumber").value("TRACK-200"))
                .andExpect(jsonPath("$.status").value("DELIVERED_TO_LOCKER"))
                .andExpect(jsonPath("$.receiverFirstName").value("Mila"))
                .andExpect(jsonPath("$.buildingAddress").value("123 Main St"));

        verify(packageService).getByTrackingNumber("TRACK-200");
    }

    @Test
    void getAllPackages_returnsAllPackages() throws Exception {
        // Arrange
        List<Package> packages = List.of(
                createPackage(3L, "TRACK-300", PackageStatus.PENDING),
                createPackage(4L, "TRACK-301", PackageStatus.PICKED_UP)
        );
        when(packageService.getAllPackages()).thenReturn(packages);

        // Act + Assert
        mockMvc.perform(get("/packages/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].trackingNumber").value("TRACK-300"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].trackingNumber").value("TRACK-301"))
                .andExpect(jsonPath("$[1].status").value("PICKED_UP"));

        verify(packageService).getAllPackages();
    }

    @Test
    void getAllPackagesByReceiver_returnsReceiverPackages() throws Exception {
        // Arrange
        Long receiverId = 11L;
        List<Package> packages = List.of(
                createPackage(5L, "TRACK-400", PackageStatus.PENDING),
                createPackage(6L, "TRACK-401", PackageStatus.DELIVERED_TO_LOCKER)
        );
        when(packageService.getAllPackagesByReceiver(receiverId)).thenReturn(packages);

        // Act + Assert
        mockMvc.perform(get("/packages/receiver/{receiverId}", receiverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].receiverId").value(11))
                .andExpect(jsonPath("$[0].trackingNumber").value("TRACK-400"))
                .andExpect(jsonPath("$[1].trackingNumber").value("TRACK-401"));

        verify(packageService).getAllPackagesByReceiver(receiverId);
    }

    private Package createPackage(Long id, String trackingNumber, PackageStatus status) {
        Building building = Building.builder()
                .id(21L)
                .name("Main Building")
                .address("123 Main St")
                .description("Primary pickup point")
                .build();

        Locker locker = Locker.builder()
                .id(31L)
                .lockerNumber("L-01")
                .size(LockerSize.LARGE)
                .status(LockerStatus.OCCUPIED)
                .building(building)
                .build();

        User receiver = User.builder()
                .id(11L)
                .firstName("Mila")
                .lastName("Bos")
                .email("mila@example.com")
                .role(UserRole.EMPLOYEE)
                .build();

        return Package.builder()
                .id(id)
                .trackingNumber(trackingNumber)
                .description("Laptop")
                .length(new BigDecimal("30.50"))
                .width(new BigDecimal("20.50"))
                .height(new BigDecimal("10.00"))
                .status(status)
                .receiver(receiver)
                .locker(locker)
                .deliveredAt(LocalDateTime.of(2026, 5, 7, 10, 15))
                .pickedUpAt(status == PackageStatus.PICKED_UP ? LocalDateTime.of(2026, 5, 7, 12, 30) : null)
                .build();
    }
}
