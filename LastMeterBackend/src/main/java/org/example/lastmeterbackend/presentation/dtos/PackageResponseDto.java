package org.example.lastmeterbackend.presentation.dtos;

import lombok.Builder;
import lombok.Getter;
import org.example.lastmeterbackend.domain.enums.PackageStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PackageResponseDto {

    private Long id;
    private String trackingNumber;
    private String description;

    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;

    private PackageStatus status;

    private Long receiverId;
    private String receiverFirstName;
    private String receiverLastName;
    private String receiverEmail;

    private Long lockerId;
    private String lockerNumber;

    private Long buildingId;
    private String buildingName;
    private String buildingAddress;

    private LocalDateTime deliveredAt;
    private LocalDateTime pickedUpAt;
}
