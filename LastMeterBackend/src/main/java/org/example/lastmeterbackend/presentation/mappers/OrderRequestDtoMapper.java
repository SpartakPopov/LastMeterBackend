package org.example.lastmeterbackend.presentation.mappers;

import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.presentation.dtos.OrderRequestResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OrderRequestDtoMapper {

    public OrderRequestResponseDto toDto(OrderRequest orderRequest) {
        return OrderRequestResponseDto.builder()
                .id(orderRequest.getId())
                .description(orderRequest.getDescription())
                .productLinks(orderRequest.getProductLinks())
                .quantity(orderRequest.getQuantity())
                .requestedById(orderRequest.getRequestedBy() != null ? orderRequest.getRequestedBy().getId() : null)
                .requestedByFirstName(orderRequest.getRequestedBy() != null ? orderRequest.getRequestedBy().getFirstName() : null)
                .requestedByLastName(orderRequest.getRequestedBy() != null ? orderRequest.getRequestedBy().getLastName() : null)
                .requestedForId(orderRequest.getRequestedFor() != null ? orderRequest.getRequestedFor().getId() : null)
                .requestedForFirstName(orderRequest.getRequestedFor() != null ? orderRequest.getRequestedFor().getFirstName() : null)
                .requestedForLastName(orderRequest.getRequestedFor() != null ? orderRequest.getRequestedFor().getLastName() : null)
                .status(orderRequest.getStatus())
                .managerNotes(orderRequest.getManagerNotes())
                .createdAt(orderRequest.getCreatedAt())
                .updatedAt(orderRequest.getUpdatedAt())
                .groupId(orderRequest.getGroupId())
                .build();
    }
}
