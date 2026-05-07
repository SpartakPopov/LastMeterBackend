package org.example.lastmeterbackend.presentation.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequestCreationDto {
    private String description;
    private String productLinks;
    private Integer quantity;
    private Long requestedById;
    private Long requestedForId;
}
