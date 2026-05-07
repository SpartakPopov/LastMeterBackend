package org.example.lastmeterbackend.presentation.dtos;

import lombok.Builder;
import lombok.Getter;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderRequestResponseDto {
    private Long id;
    private String description;
    private String productLinks;
    private Integer quantity;

    private Long requestedById;
    private String requestedByFirstName;
    private String requestedByLastName;

    private Long requestedForId;
    private String requestedForFirstName;
    private String requestedForLastName;

    private OrderRequestStatus status;
    private String managerNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
