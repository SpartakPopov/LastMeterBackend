package org.example.lastmeterbackend.presentation.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.lastmeterbackend.domain.enums.PackageStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PackageCreationDto {

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
    //picked up at Note: this is can be optional depending on the status of the package
    private LocalDateTime pickedUpAt;

}


