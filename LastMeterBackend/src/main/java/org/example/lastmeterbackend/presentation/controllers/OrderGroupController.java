package org.example.lastmeterbackend.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lastmeterbackend.business.services.OrderGroupService;
import org.example.lastmeterbackend.domain.models.OrderGroup;
import org.example.lastmeterbackend.presentation.dtos.OrderGroupCreationDto;
import org.example.lastmeterbackend.presentation.dtos.OrderGroupResponseDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestFulfillDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestResponseDto;
import org.example.lastmeterbackend.presentation.mappers.OrderRequestDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-groups")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Order Groups", description = "Batch grouping of approved order requests for bulk fulfilment by admins")
public class OrderGroupController {

    private final OrderGroupService orderGroupService;
    private final OrderRequestDtoMapper orderRequestDtoMapper;

    public OrderGroupController(OrderGroupService orderGroupService,
                                OrderRequestDtoMapper orderRequestDtoMapper) {
        this.orderGroupService = orderGroupService;
        this.orderRequestDtoMapper = orderRequestDtoMapper;
    }

    @Operation(summary = "Create an order group", description = "Groups a set of APPROVED order requests under a named group for batch fulfilment.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group created"),
        @ApiResponse(responseCode = "400", description = "Invalid request or non-approved order requests included")
    })
    @PostMapping
    public OrderGroupResponseDto createGroup(@RequestBody OrderGroupCreationDto dto) {
        OrderGroup group = orderGroupService.createGroup(dto.getName(), dto.getOrderRequestIds());
        return toDto(group);
    }

    @Operation(summary = "Get all order groups", description = "Returns all order groups with their contained order requests.")
    @ApiResponse(responseCode = "200", description = "List of all order groups")
    @GetMapping
    public List<OrderGroupResponseDto> getAllGroups() {
        return orderGroupService.getAllGroups().stream().map(this::toDto).toList();
    }

    @Operation(summary = "Get order group by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Group found"),
        @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @GetMapping("/{id}")
    public OrderGroupResponseDto getGroupById(
            @Parameter(description = "ID of the order group") @PathVariable Long id) {
        return toDto(orderGroupService.getGroupById(id));
    }

    @Operation(summary = "Fulfil an entire order group", description = "Creates packages for every order request in the group in a single operation. All requests transition to ORDERED.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Group fulfilled, packages created"),
        @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @PostMapping("/{id}/fulfill")
    public ResponseEntity<Void> fulfillGroup(
            @Parameter(description = "ID of the order group") @PathVariable Long id,
            @RequestBody OrderRequestFulfillDto dto) {
        orderGroupService.fulfillGroup(id, dto.getPackages());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an order group", description = "Removes the group container. The underlying order requests are not deleted.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Group deleted"),
        @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @Parameter(description = "ID of the order group") @PathVariable Long id) {
        orderGroupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    private OrderGroupResponseDto toDto(OrderGroup group) {
        List<OrderRequestResponseDto> requests = group.getOrderRequests() != null
                ? group.getOrderRequests().stream().map(orderRequestDtoMapper::toDto).toList()
                : List.of();
        return OrderGroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .requestedById(group.getRequestedBy() != null ? group.getRequestedBy().getId() : null)
                .requestedByFirstName(group.getRequestedBy() != null ? group.getRequestedBy().getFirstName() : null)
                .requestedByLastName(group.getRequestedBy() != null ? group.getRequestedBy().getLastName() : null)
                .orderRequests(requests)
                .createdAt(group.getCreatedAt())
                .build();
    }
}
