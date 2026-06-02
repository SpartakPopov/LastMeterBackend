package org.example.lastmeterbackend.domain.repositories;

import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;
import org.example.lastmeterbackend.domain.models.OrderRequest;

import java.util.List;
import java.util.Optional;

public interface OrderRequestRepository {
    OrderRequest save(OrderRequest orderRequest);
    Optional<OrderRequest> findById(Long id);
    List<OrderRequest> findAll();
    List<OrderRequest> findAllById(List<Long> ids);
    List<OrderRequest> findByRequestedById(Long userId);
    OrderRequest updateStatus(Long id, OrderRequestStatus status, String managerNotes);
    OrderRequest fulfill(Long id);
    OrderRequest updateFields(Long id, String description, String productLinks, Integer quantity);
    void setGroup(Long requestId, Long groupId);
}
