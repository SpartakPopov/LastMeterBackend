package org.example.lastmeterbackend.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lastmeterbackend.business.services.OrderRequestService;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestCreationDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestFulfillDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestRejectDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestResponseDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestUpdateDto;
import org.example.lastmeterbackend.presentation.mappers.OrderRequestDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order-requests")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Order Requests", description = "Employee order request lifecycle — create, approve, reject, and fulfil purchase requests")
public class OrderRequestController {

    private final OrderRequestService orderRequestService;
    private final OrderRequestDtoMapper dtoMapper;

    public OrderRequestController(OrderRequestService orderRequestService,
                                  OrderRequestDtoMapper dtoMapper) {
        this.orderRequestService = orderRequestService;
        this.dtoMapper = dtoMapper;
    }

    @Operation(summary = "Create an order request", description = "Submits a new purchase request on behalf of an employee. Status starts as PENDING.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order request created"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public OrderRequestResponseDto createOrderRequest(@RequestBody OrderRequestCreationDto dto) {
        return dtoMapper.toDto(orderRequestService.createOrderRequest(
                dto.getRequestedById(), dto.getRequestedForId(),
                dto.getDescription(), dto.getProductLinks(), dto.getQuantity()
        ));
    }

    @Operation(summary = "Get all order requests", description = "Returns every order request in the system. Intended for admin/manager views.")
    @ApiResponse(responseCode = "200", description = "List of all order requests")
    @GetMapping
    public List<OrderRequestResponseDto> getAllOrderRequests() {
        return orderRequestService.getAllOrderRequests().stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get order requests by user", description = "Returns all order requests submitted by or for the specified user.")
    @ApiResponse(responseCode = "200", description = "List of order requests for the user")
    @GetMapping("/my/{userId}")
    public List<OrderRequestResponseDto> getMyOrderRequests(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        return orderRequestService.getOrderRequestsByUser(userId).stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get order request by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order request found"),
        @ApiResponse(responseCode = "404", description = "Order request not found")
    })
    @GetMapping("/{id}")
    public OrderRequestResponseDto getOrderRequestById(
            @Parameter(description = "ID of the order request") @PathVariable Long id) {
        return dtoMapper.toDto(orderRequestService.getOrderRequestById(id));
    }

    @Operation(summary = "Update an order request", description = "Updates description, product links, or quantity. Only permitted while status is PENDING.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order request updated"),
        @ApiResponse(responseCode = "404", description = "Order request not found"),
        @ApiResponse(responseCode = "409", description = "Order request is no longer in a modifiable state")
    })
    @PatchMapping("/{id}")
    public OrderRequestResponseDto updateOrderRequest(
            @Parameter(description = "ID of the order request") @PathVariable Long id,
            @RequestBody OrderRequestUpdateDto dto) {
        return dtoMapper.toDto(orderRequestService.updateOrderRequest(
                id, dto.getDescription(), dto.getProductLinks(), dto.getQuantity()));
    }

    @Operation(summary = "Approve an order request", description = "Transitions status from PENDING to APPROVED. Manager notes are optional but recommended.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order request approved"),
        @ApiResponse(responseCode = "404", description = "Order request not found"),
        @ApiResponse(responseCode = "409", description = "Order request is not in PENDING status")
    })
    @PatchMapping("/{id}/approve")
    public OrderRequestResponseDto approveOrderRequest(
            @Parameter(description = "ID of the order request") @PathVariable Long id,
            @RequestBody OrderRequestRejectDto dto) {
        return dtoMapper.toDto(orderRequestService.approveOrderRequest(id, dto.getManagerNotes()));
    }

    @Operation(summary = "Reject an order request", description = "Transitions status from PENDING to REJECTED. Manager notes explaining the rejection are required.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order request rejected"),
        @ApiResponse(responseCode = "404", description = "Order request not found"),
        @ApiResponse(responseCode = "409", description = "Order request is not in PENDING status")
    })
    @PatchMapping("/{id}/reject")
    public OrderRequestResponseDto rejectOrderRequest(
            @Parameter(description = "ID of the order request") @PathVariable Long id,
            @RequestBody OrderRequestRejectDto dto) {
        return dtoMapper.toDto(orderRequestService.rejectOrderRequest(id, dto.getManagerNotes()));
    }

    @Operation(summary = "Fulfil an order request", description = "Creates the specified packages linked to the request recipient and transitions status to ORDERED. Request must be APPROVED.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order fulfilled, packages created"),
        @ApiResponse(responseCode = "404", description = "Order request not found"),
        @ApiResponse(responseCode = "409", description = "Order request is not in APPROVED status")
    })
    @PatchMapping("/{id}/fulfill")
    public OrderRequestResponseDto fulfillOrderRequest(
            @Parameter(description = "ID of the order request") @PathVariable Long id,
            @RequestBody OrderRequestFulfillDto dto) {
        return dtoMapper.toDto(orderRequestService.fulfillOrderRequest(id, dto.getPackages()));
    }
}
