package org.example.lastmeterbackend.presentation.mappers;

import org.example.lastmeterbackend.presentation.dtos.PackageCreationDto;
import org.example.lastmeterbackend.presentation.dtos.PackageResponseDto;
import org.example.lastmeterbackend.domain.models.Package;
import org.springframework.stereotype.Component;

@Component
public class PackageDtoMapper {

   public PackageCreationDto toCreationDto(Package pkg) {
        return PackageCreationDto.builder()
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

                //can be null since the package is not delivered yet
                .deliveredAt(
                        pkg.getDeliveredAt() != null ? pkg.getDeliveredAt() : null)

                //can be null since the package is not picked up yet
                .pickedUpAt(
                        pkg.getPickedUpAt() != null ? pkg.getPickedUpAt() : null
                )
                .build();
    }


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
                //this will not be null since this for when the package gets delivered
                .deliveredAt(pkg.getDeliveredAt())
                //this will not be null since this will be updated when the package 
                //gets picked up and by defualt it is null when the order was created.
                .pickedUpAt(pkg.getPickedUpAt())
                .build();
    }
}