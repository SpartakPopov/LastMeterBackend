package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.OrderRequestService;
import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;
import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.domain.repositories.OrderRequestRepository;
import org.example.lastmeterbackend.presentation.dtos.FulfillPackageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderRequestServiceImpl implements OrderRequestService {

    private final OrderRequestRepository orderRequestRepository;
    private final PackageService packageService;

    public OrderRequestServiceImpl(OrderRequestRepository orderRequestRepository,
                                   PackageService packageService) {
        this.orderRequestRepository = orderRequestRepository;
        this.packageService = packageService;
    }

    @Override
    public OrderRequest createOrderRequest(Long requestedById, Long requestedForId, String description, String productLinks, Integer quantity) {
        User requestedBy = User.builder().id(requestedById).build();
        User requestedFor = requestedForId != null ? User.builder().id(requestedForId).build() : requestedBy;

        OrderRequest orderRequest = OrderRequest.builder()
                .description(description)
                .productLinks(productLinks)
                .quantity(quantity != null ? quantity : 1)
                .requestedBy(requestedBy)
                .requestedFor(requestedFor)
                .status(OrderRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return orderRequestRepository.save(orderRequest);
    }

    @Override
    public List<OrderRequest> getAllOrderRequests() {
        return orderRequestRepository.findAll();
    }

    @Override
    public List<OrderRequest> getOrderRequestsByUser(Long userId) {
        return orderRequestRepository.findByRequestedById(userId);
    }

    @Override
    public OrderRequest getOrderRequestById(Long id) {
        return orderRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderRequest not found with id: " + id));
    }

    @Override
    public OrderRequest approveOrderRequest(Long id, String managerNotes) {
        return orderRequestRepository.updateStatus(id, OrderRequestStatus.APPROVED, managerNotes);
    }

    @Override
    public OrderRequest rejectOrderRequest(Long id, String managerNotes) {
        return orderRequestRepository.updateStatus(id, OrderRequestStatus.REJECTED, managerNotes);
    }

    @Override
    @Transactional
    public OrderRequest fulfillOrderRequest(Long id, List<FulfillPackageDto> packages) {
        OrderRequest orderRequest = orderRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderRequest not found with id: " + id));

        for (FulfillPackageDto dto : packages) {
            packageService.createPackage(
                    dto.getTrackingNumber(), dto.getDescription(),
                    dto.getLength(), dto.getWidth(), dto.getHeight(),
                    null, orderRequest.getRequestedFor().getId()
            );
        }

        return orderRequestRepository.fulfill(id);
    }

    @Override
    public OrderRequest updateOrderRequest(Long id, String description, String productLinks, Integer quantity) {
        return orderRequestRepository.updateFields(id, description, productLinks, quantity);
    }
}
