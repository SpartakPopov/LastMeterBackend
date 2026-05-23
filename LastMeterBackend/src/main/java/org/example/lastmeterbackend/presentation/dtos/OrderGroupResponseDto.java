package org.example.lastmeterbackend.presentation.dtos;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderGroupResponseDto {
    private Long id;
    private String name;
    private Long requestedById;
    private String requestedByFirstName;
    private String requestedByLastName;
    private List<OrderRequestResponseDto> orderRequests;
    private LocalDateTime createdAt;
}
