package org.example.lastmeterbackend.unitTests;

import org.example.lastmeterbackend.business.serviceImplementations.OrderRequestServiceImpl;
import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.domain.repositories.OrderRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRequestServiceUnitTest {

    @Mock
    private OrderRequestRepository orderRequestRepository;

    @Mock
    private PackageService packageService;

    @InjectMocks
    private OrderRequestServiceImpl orderRequestService;

    @Test
    void createOrderRequest_returnsSavedOrderRequest() {
        // Arrange
        OrderRequest newOrderRequest = createOrderRequest("Monitors", OrderRequestStatus.PENDING);
        when(orderRequestRepository.save(newOrderRequest)).thenReturn(newOrderRequest);

        // Act
        OrderRequest result = orderRequestService.createOrderRequest(newOrderRequest);

        // Assert
        assertEquals(newOrderRequest, result);
        verify(orderRequestRepository).save(newOrderRequest);
    }

    @Test
    void createOrderRequest_throwsExceptionWhenRepositoryFails() {
        // Arrange
        OrderRequest newOrderRequest = createOrderRequest("Monitors", OrderRequestStatus.PENDING);
        when(orderRequestRepository.save(newOrderRequest)).thenThrow(new RuntimeException("save failed"));

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderRequestService.createOrderRequest(newOrderRequest)
        );

        // Assert
        assertEquals("save failed", exception.getMessage());
        verify(orderRequestRepository).save(newOrderRequest);
    }

    @Test
    void getAllOrderRequests_returnsAllOrderRequests() {
        // Arrange
        List<OrderRequest> expectedOrderRequests = List.of(
                createOrderRequest("Monitors", OrderRequestStatus.PENDING),
                createOrderRequest("Keyboards", OrderRequestStatus.APPROVED)
        );
        when(orderRequestRepository.findAll()).thenReturn(expectedOrderRequests);

        // Act
        List<OrderRequest> result = orderRequestService.getAllOrderRequests();

        // Assert
        assertEquals(expectedOrderRequests, result);
        verify(orderRequestRepository).findAll();
    }

    @Test
    void getOrderRequestsByUser_returnsUserOrderRequests() {
        // Arrange
        Long userId = 15L;
        List<OrderRequest> expectedOrderRequests = List.of(
                createOrderRequest("Mouse", OrderRequestStatus.PENDING),
                createOrderRequest("Chair", OrderRequestStatus.REJECTED)
        );
        when(orderRequestRepository.findByRequestedById(userId)).thenReturn(expectedOrderRequests);

        // Act
        List<OrderRequest> result = orderRequestService.getOrderRequestsByUser(userId);

        // Assert
        assertEquals(expectedOrderRequests, result);
        verify(orderRequestRepository).findByRequestedById(userId);
    }

    @Test
    void getOrderRequestById_returnsOrderRequestWhenItExists() {
        // Arrange
        Long id = 20L;
        OrderRequest expectedOrderRequest = createOrderRequest("Desk", OrderRequestStatus.PENDING);
        when(orderRequestRepository.findById(id)).thenReturn(Optional.of(expectedOrderRequest));

        // Act
        OrderRequest result = orderRequestService.getOrderRequestById(id);

        // Assert
        assertEquals(expectedOrderRequest, result);
        verify(orderRequestRepository).findById(id);
    }

    @Test
    void getOrderRequestById_throwsExceptionWhenItDoesNotExist() {
        // Arrange
        Long id = 20L;
        when(orderRequestRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderRequestService.getOrderRequestById(id)
        );

        // Assert
        assertEquals("OrderRequest not found with id: " + id, exception.getMessage());
        verify(orderRequestRepository).findById(id);
    }

    @Test
    void approveOrderRequest_returnsUpdatedOrderRequest() {
        // Arrange
        Long id = 30L;
        String managerNotes = "Approved";
        OrderRequest approvedOrderRequest = createOrderRequest("Approved request", OrderRequestStatus.APPROVED);
        approvedOrderRequest.setManagerNotes(managerNotes);
        when(orderRequestRepository.updateStatus(id, OrderRequestStatus.APPROVED, managerNotes))
                .thenReturn(approvedOrderRequest);

        // Act
        OrderRequest result = orderRequestService.approveOrderRequest(id, managerNotes);

        // Assert
        assertEquals(approvedOrderRequest, result);
        verify(orderRequestRepository).updateStatus(id, OrderRequestStatus.APPROVED, managerNotes);
    }

    @Test
    void rejectOrderRequest_returnsUpdatedOrderRequest() {
        // Arrange
        Long id = 31L;
        String managerNotes = "Rejected";
        OrderRequest rejectedOrderRequest = createOrderRequest("Rejected request", OrderRequestStatus.REJECTED);
        rejectedOrderRequest.setManagerNotes(managerNotes);
        when(orderRequestRepository.updateStatus(id, OrderRequestStatus.REJECTED, managerNotes))
                .thenReturn(rejectedOrderRequest);

        // Act
        OrderRequest result = orderRequestService.rejectOrderRequest(id, managerNotes);

        // Assert
        assertEquals(rejectedOrderRequest, result);
        verify(orderRequestRepository).updateStatus(id, OrderRequestStatus.REJECTED, managerNotes);
    }

    @Test
    void fulfillOrderRequest_createsPackagesAndReturnsFulfilledOrderRequest() {
        // Arrange
        Long id = 40L;
        List<String> trackingNumbers = List.of("TRACK-500", "TRACK-501");
        User requestedFor = createUser();
        OrderRequest orderRequest = createOrderRequest("Approved request", OrderRequestStatus.APPROVED);
        orderRequest.setRequestedFor(requestedFor);
        OrderRequest fulfilledOrderRequest = createOrderRequest("Approved request", OrderRequestStatus.ORDERED);
        fulfilledOrderRequest.setRequestedFor(requestedFor);
        when(orderRequestRepository.findById(id)).thenReturn(Optional.of(orderRequest));
        when(orderRequestRepository.fulfill(id)).thenReturn(fulfilledOrderRequest);

        // Act
        OrderRequest result = orderRequestService.fulfillOrderRequest(id, trackingNumbers);

        // Assert
        assertEquals(fulfilledOrderRequest, result);

        ArgumentCaptor<Package> packageCaptor = ArgumentCaptor.forClass(Package.class);
        verify(packageService, times(2)).createPackage(packageCaptor.capture());
        List<Package> createdPackages = packageCaptor.getAllValues();
        assertEquals("TRACK-500", createdPackages.get(0).getTrackingNumber());
        assertEquals("TRACK-501", createdPackages.get(1).getTrackingNumber());
        assertEquals(PackageStatus.PENDING, createdPackages.get(0).getStatus());
        assertEquals(PackageStatus.PENDING, createdPackages.get(1).getStatus());
        assertEquals(requestedFor, createdPackages.get(0).getReceiver());
        assertEquals(requestedFor, createdPackages.get(1).getReceiver());

        verify(orderRequestRepository).findById(id);
        verify(orderRequestRepository).fulfill(id);
        verifyNoMoreInteractions(orderRequestRepository);
    }

    @Test
    void fulfillOrderRequest_throwsExceptionWhenOrderRequestDoesNotExist() {
        // Arrange
        Long id = 40L;
        List<String> trackingNumbers = List.of("TRACK-500", "TRACK-501");
        when(orderRequestRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderRequestService.fulfillOrderRequest(id, trackingNumbers)
        );

        // Assert
        assertEquals("OrderRequest not found with id: " + id, exception.getMessage());
        verify(orderRequestRepository).findById(id);
        verifyNoInteractions(packageService);
    }

    @Test
    void fulfillOrderRequest_throwsExceptionWhenPackageCreationFails() {
        // Arrange
        Long id = 40L;
        List<String> trackingNumbers = List.of("TRACK-500");
        User requestedFor = createUser();
        OrderRequest orderRequest = createOrderRequest("Approved request", OrderRequestStatus.APPROVED);
        orderRequest.setRequestedFor(requestedFor);
        when(orderRequestRepository.findById(id)).thenReturn(Optional.of(orderRequest));
        when(packageService.createPackage(ArgumentMatchers.any(Package.class)))
                .thenThrow(new RuntimeException("package creation failed"));

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderRequestService.fulfillOrderRequest(id, trackingNumbers)
        );

        // Assert
        assertEquals("package creation failed", exception.getMessage());
        verify(orderRequestRepository).findById(id);
        verify(packageService).createPackage(ArgumentMatchers.any(Package.class));
    }

    private OrderRequest createOrderRequest(String description, OrderRequestStatus status) {
        return OrderRequest.builder()
                .description(description)
                .productLinks("https://example.com/" + description.toLowerCase().replace(" ", "-"))
                .quantity(2)
                .status(status)
                .build();
    }

    private User createUser() {
        return User.builder()
                .firstName("Mila")
                .lastName("Bos")
                .email("mila@example.com")
                .build();
    }
}
