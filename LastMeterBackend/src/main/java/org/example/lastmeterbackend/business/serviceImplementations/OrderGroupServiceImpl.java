package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.OrderGroupService;
import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.models.OrderGroup;
import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.domain.repositories.OrderGroupRepository;
import org.example.lastmeterbackend.domain.repositories.OrderRequestRepository;
import org.example.lastmeterbackend.presentation.dtos.FulfillPackageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderGroupServiceImpl implements OrderGroupService {

    private final OrderGroupRepository orderGroupRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final PackageService packageService;

    public OrderGroupServiceImpl(OrderGroupRepository orderGroupRepository,
                                 OrderRequestRepository orderRequestRepository,
                                 PackageService packageService) {
        this.orderGroupRepository = orderGroupRepository;
        this.orderRequestRepository = orderRequestRepository;
        this.packageService = packageService;
    }

    @Override
    @Transactional
    public OrderGroup createGroup(String name, List<Long> orderRequestIds) {
        List<OrderRequest> requests = orderRequestRepository.findAllById(orderRequestIds);

        OrderGroup group = OrderGroup.builder()
                .name(name)
                .requestedBy(requests.isEmpty() ? null : requests.get(0).getRequestedBy())
                .createdAt(LocalDateTime.now())
                .build();

        OrderGroup saved = orderGroupRepository.save(group);

        for (OrderRequest req : requests) {
            orderRequestRepository.setGroup(req.getId(), saved.getId());
        }

        return orderGroupRepository.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("OrderGroup not found after creation"));
    }

    @Override
    public List<OrderGroup> getAllGroups() {
        return orderGroupRepository.findAll();
    }

    @Override
    public OrderGroup getGroupById(Long id) {
        return orderGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderGroup not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) {
        orderGroupRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void fulfillGroup(Long groupId, List<FulfillPackageDto> packages) {
        OrderGroup group = orderGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("OrderGroup not found with id: " + groupId));

        for (OrderRequest req : group.getOrderRequests()) {
            for (FulfillPackageDto dto : packages) {
                packageService.createPackage(
                        dto.getTrackingNumber(), dto.getDescription(),
                        dto.getLength(), dto.getWidth(), dto.getHeight(),
                        null, req.getRequestedFor().getId()
                );
            }
            orderRequestRepository.fulfill(req.getId());
        }
    }
}
