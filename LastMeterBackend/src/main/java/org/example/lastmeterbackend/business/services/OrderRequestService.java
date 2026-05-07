package org.example.lastmeterbackend.business.services;

import org.example.lastmeterbackend.domain.models.OrderRequest;

import java.util.List;

public interface OrderRequestService {
    OrderRequest createOrderRequest(OrderRequest orderRequest);
    List<OrderRequest> getAllOrderRequests();
    List<OrderRequest> getOrderRequestsByUser(Long userId);
    OrderRequest getOrderRequestById(Long id);
    OrderRequest approveOrderRequest(Long id, String managerNotes);
    OrderRequest rejectOrderRequest(Long id, String managerNotes);
    OrderRequest fulfillOrderRequest(Long id, List<String> trackingNumbers);
}
