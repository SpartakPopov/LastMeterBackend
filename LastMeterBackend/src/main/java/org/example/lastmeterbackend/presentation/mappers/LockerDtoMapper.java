package org.example.lastmeterbackend.presentation.mappers;

import org.example.lastmeterbackend.domain.models.Building;
import org.example.lastmeterbackend.domain.models.Locker;
import org.example.lastmeterbackend.presentation.dtos.LockerResponseDto;
import org.springframework.stereotype.Component;

@Component
public class LockerDtoMapper {

    public LockerResponseDto toDto(Locker locker) {
        Building building = locker.getBuilding();
        return LockerResponseDto.builder()
                .id(locker.getId())
                .lockerNumber(locker.getLockerNumber())
                .size(locker.getSize() != null ? locker.getSize().name() : null)
                .status(locker.getStatus() != null ? locker.getStatus().name() : null)
                .buildingName(building != null ? building.getName() : null)
                .buildingAddress(building != null ? building.getAddress() : null)
                .build();
    }
}
