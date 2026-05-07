package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.business.services.OrderRequestService;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;
import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.domain.models.User;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestCreationDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestFulfillDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestRejectDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestResponseDto;
import org.example.lastmeterbackend.presentation.mappers.OrderRequestDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order-requests")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderRequestController {

    private final OrderRequestService orderRequestService;
    private final OrderRequestDtoMapper dtoMapper;

    public OrderRequestController(OrderRequestService orderRequestService,
                                  OrderRequestDtoMapper dtoMapper) {
        this.orderRequestService = orderRequestService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public OrderRequestResponseDto createOrderRequest(@RequestBody OrderRequestCreationDto dto) {
        User requestedBy = User.builder().id(dto.getRequestedById()).build();
        User requestedFor = dto.getRequestedForId() != null
                ? User.builder().id(dto.getRequestedForId()).build()
                : requestedBy;

        OrderRequest orderRequest = OrderRequest.builder()
                .description(dto.getDescription())
                .productLinks(dto.getProductLinks())
                .quantity(dto.getQuantity() != null ? dto.getQuantity() : 1)
                .requestedBy(requestedBy)
                .requestedFor(requestedFor)
                .status(OrderRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return dtoMapper.toDto(orderRequestService.createOrderRequest(orderRequest));
    }

    @GetMapping
    public List<OrderRequestResponseDto> getAllOrderRequests() {
        return orderRequestService.getAllOrderRequests().stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/my/{userId}")
    public List<OrderRequestResponseDto> getMyOrderRequests(@PathVariable Long userId) {
        return orderRequestService.getOrderRequestsByUser(userId).stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderRequestResponseDto getOrderRequestById(@PathVariable Long id) {
        return dtoMapper.toDto(orderRequestService.getOrderRequestById(id));
    }

    @PatchMapping("/{id}/approve")
    public OrderRequestResponseDto approveOrderRequest(
            @PathVariable Long id,
            @RequestBody OrderRequestRejectDto dto) {
        return dtoMapper.toDto(orderRequestService.approveOrderRequest(id, dto.getManagerNotes()));
    }

    @PatchMapping("/{id}/reject")
    public OrderRequestResponseDto rejectOrderRequest(
            @PathVariable Long id,
            @RequestBody OrderRequestRejectDto dto) {
        return dtoMapper.toDto(orderRequestService.rejectOrderRequest(id, dto.getManagerNotes()));
    }

    @PatchMapping("/{id}/fulfill")
    public OrderRequestResponseDto fulfillOrderRequest(
            @PathVariable Long id,
            @RequestBody OrderRequestFulfillDto dto) {
        return dtoMapper.toDto(orderRequestService.fulfillOrderRequest(id, dto.getTrackingNumbers()));
    }
}
