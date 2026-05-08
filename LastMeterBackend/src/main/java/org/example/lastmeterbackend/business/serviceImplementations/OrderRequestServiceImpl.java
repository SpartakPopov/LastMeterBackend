package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.OrderRequestService;
import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.repositories.OrderRequestRepository;
import org.example.lastmeterbackend.presentation.dtos.FulfillPackageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderRequest createOrderRequest(OrderRequest orderRequest) {
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
            Package pkg = Package.builder()
                    .trackingNumber(dto.getTrackingNumber())
                    .description(dto.getDescription())
                    .length(dto.getLength())
                    .width(dto.getWidth())
                    .height(dto.getHeight())
                    .status(PackageStatus.PENDING)
                    .receiver(orderRequest.getRequestedFor())
                    .build();
            packageService.createPackage(pkg);
        }

        return orderRequestRepository.fulfill(id);
    }
}
