package org.example.lastmeterbackend.presentation.mappers;

import org.example.lastmeterbackend.presentation.dtos.PackageResponseDto;
import org.example.lastmeterbackend.domain.models.Package;
import org.springframework.stereotype.Component;

@Component
public class PackageResponseDtoMapper {

    public PackageResponseDto toDto(Package pkg) {
        return PackageResponseDto.builder()
                .id(pkg.getId())
                .trackingNumber(pkg.getTrackingNumber())
                .description(pkg.getDescription())
                .length(pkg.getLength())
                .width(pkg.getWidth())
                .height(pkg.getHeight())
                .status(pkg.getStatus())

                .receiverId(pkg.getReceiver() != null ? pkg.getReceiver().getId() : null)
                .receiverFirstName(pkg.getReceiver() != null ? pkg.getReceiver().getFirstName() : null)
                .receiverLastName(pkg.getReceiver() != null ? pkg.getReceiver().getLastName() : null)
                .receiverEmail(pkg.getReceiver() != null ? pkg.getReceiver().getEmail() : null)

                .lockerId(pkg.getLocker() != null ? pkg.getLocker().getId() : null)
                .lockerNumber(pkg.getLocker() != null ? pkg.getLocker().getLockerNumber() : null)

                .buildingId(
                        pkg.getLocker() != null && pkg.getLocker().getBuilding() != null
                                ? pkg.getLocker().getBuilding().getId()
                                : null
                )
                .buildingName(
                        pkg.getLocker() != null && pkg.getLocker().getBuilding() != null
                                ? pkg.getLocker().getBuilding().getName()
                                : null
                )
                .buildingAddress(
                        pkg.getLocker() != null && pkg.getLocker().getBuilding() != null
                                ? pkg.getLocker().getBuilding().getAddress()
                                : null
                )

                .deliveredAt(pkg.getDeliveredAt())
                .pickedUpAt(pkg.getPickedUpAt())
                .build();
    }
}