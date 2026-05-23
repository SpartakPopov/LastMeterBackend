package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.business.services.OrderGroupService;
import org.example.lastmeterbackend.domain.models.OrderGroup;
import org.example.lastmeterbackend.presentation.dtos.OrderGroupCreationDto;
import org.example.lastmeterbackend.presentation.dtos.OrderGroupResponseDto;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestResponseDto;
import org.example.lastmeterbackend.presentation.mappers.OrderRequestDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-groups")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderGroupController {

    private final OrderGroupService orderGroupService;
    private final OrderRequestDtoMapper orderRequestDtoMapper;

    public OrderGroupController(OrderGroupService orderGroupService,
                                OrderRequestDtoMapper orderRequestDtoMapper) {
        this.orderGroupService = orderGroupService;
        this.orderRequestDtoMapper = orderRequestDtoMapper;
    }

    @PostMapping
    public OrderGroupResponseDto createGroup(@RequestBody OrderGroupCreationDto dto) {
        OrderGroup group = orderGroupService.createGroup(dto.getName(), dto.getRequestedById(), dto.getOrderRequestIds());
        return toDto(group);
    }

    @GetMapping
    public List<OrderGroupResponseDto> getAllGroups() {
        return orderGroupService.getAllGroups().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public OrderGroupResponseDto getGroupById(@PathVariable Long id) {
        return toDto(orderGroupService.getGroupById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
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
