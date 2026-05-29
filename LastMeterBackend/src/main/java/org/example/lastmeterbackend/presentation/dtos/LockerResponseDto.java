package org.example.lastmeterbackend.presentation.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LockerResponseDto {
    private Long id;
    private String lockerNumber;
    private String size;
    private String status;
    private String buildingName;
    private String buildingAddress;
}
