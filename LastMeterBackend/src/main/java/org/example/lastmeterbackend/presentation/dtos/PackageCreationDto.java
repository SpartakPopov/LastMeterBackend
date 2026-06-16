package org.example.lastmeterbackend.presentation.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lastmeterbackend.domain.enums.PackageStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request/response DTO for creating a new package")
public class PackageCreationDto {

    @Schema(description = "System-generated package ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Unique tracking number (e.g. TRK-001)", example = "TRK-001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String trackingNumber;

    @Schema(description = "Human-readable description of the package contents", example = "Wireless keyboard x2")
    private String description;

    @Schema(description = "Package length in cm", example = "30.0")
    private BigDecimal length;

    @Schema(description = "Package width in cm", example = "20.0")
    private BigDecimal width;

    @Schema(description = "Package height in cm", example = "10.0")
    private BigDecimal height;

    @Schema(description = "Initial status — defaults to PENDING when omitted", example = "PENDING")
    private PackageStatus status;

    @Schema(description = "ID of the user who will receive this package. Optional at creation.", example = "3")
    private Long receiverId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String receiverFirstName;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String receiverLastName;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String receiverEmail;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long lockerId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String lockerNumber;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long buildingId;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String buildingName;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String buildingAddress;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime deliveredAt;

    @Schema(description = "Timestamp when the receiver picked up the package. Null until status is PICKED_UP.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime pickedUpAt;
}


