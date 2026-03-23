package org.example.lastmeterbackend.DAL.mappers;

import org.example.lastmeterbackend.DAL.entities.BuildingEntity;
import org.example.lastmeterbackend.DAL.entities.LockerEntity;
import org.example.lastmeterbackend.DAL.entities.PackageEntity;
import org.example.lastmeterbackend.DAL.entities.UserEntity;
import org.example.lastmeterbackend.domain.models.Building;
import org.example.lastmeterbackend.domain.models.Locker;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class PackagePersistenceMapper {

    public Package toDomain(PackageEntity entity) {
        if (entity == null) {
            return null;
        }

        User receiver = mapUserToDomain(entity.getReceiver());
        Locker locker = mapLockerToDomain(entity.getLocker());

        return Package.builder()
                .id(entity.getId())
                .trackingNumber(entity.getTrackingNumber())
                .description(entity.getDescription())
                .length(entity.getLength())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .status(entity.getStatus())
                .receiver(receiver)
                .locker(locker)
                .deliveredAt(entity.getDeliveredAt())
                .pickedUpAt(entity.getPickedUpAt())
                .build();
    }

    public PackageEntity toEntity(Package domain) {
        if (domain == null) {
            return null;
        }

        UserEntity receiver = mapUserToEntity(domain.getReceiver());
        LockerEntity locker = mapLockerToEntity(domain.getLocker());

        return PackageEntity.builder()
                .id(domain.getId())
                .trackingNumber(domain.getTrackingNumber())
                .description(domain.getDescription())
                .length(domain.getLength())
                .width(domain.getWidth())
                .height(domain.getHeight())
                .status(domain.getStatus())
                .receiver(receiver)
                .locker(locker)
                .deliveredAt(domain.getDeliveredAt())
                .pickedUpAt(domain.getPickedUpAt())
                .build();
    }

    private User mapUserToDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }

    private UserEntity mapUserToEntity(User domain) {
        if (domain == null) {
            return null;
        }

        return UserEntity.builder()
                .id(domain.getId())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .email(domain.getEmail())
                .role(domain.getRole())
                .build();
    }

    private Locker mapLockerToDomain(LockerEntity entity) {
        if (entity == null) {
            return null;
        }

        return Locker.builder()
                .id(entity.getId())
                .lockerNumber(entity.getLockerNumber())
                .size(entity.getSize())
                .status(entity.getStatus())
                .building(mapBuildingToDomain(entity.getBuilding()))
                .build();
    }

    private LockerEntity mapLockerToEntity(Locker domain) {
        if (domain == null) {
            return null;
        }

        return LockerEntity.builder()
                .id(domain.getId())
                .lockerNumber(domain.getLockerNumber())
                .size(domain.getSize())
                .status(domain.getStatus())
                .building(mapBuildingToEntity(domain.getBuilding()))
                .build();
    }

    private Building mapBuildingToDomain(BuildingEntity entity) {
        if (entity == null) {
            return null;
        }

        return Building.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .build();
    }

    private BuildingEntity mapBuildingToEntity(Building domain) {
        if (domain == null) {
            return null;
        }

        return BuildingEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .address(domain.getAddress())
                .description(domain.getDescription())
                .build();
    }
}