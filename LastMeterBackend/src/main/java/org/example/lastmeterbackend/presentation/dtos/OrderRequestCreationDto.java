package org.example.lastmeterbackend.presentation.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "Request body for creating a new order request")
public class OrderRequestCreationDto {

    @Schema(description = "What the employee wants to order", example = "Ergonomic mouse for home office", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "URL(s) to the product(s) — comma-separated if multiple", example = "https://example.com/product/123")
    private String productLinks;

    @Schema(description = "Number of items requested", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(description = "ID of the user submitting this request", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long requestedById;

    @Schema(description = "ID of the user who will receive the ordered items (can differ from requester)", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long requestedForId;
}
