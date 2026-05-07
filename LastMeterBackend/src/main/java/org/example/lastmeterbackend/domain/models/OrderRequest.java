package org.example.lastmeterbackend.domain.models;

import lombok.*;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderRequest {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String description;
    private String productLinks;
    private Integer quantity;

    private User requestedBy;
    private User requestedFor;

    private OrderRequestStatus status;
    private String managerNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
