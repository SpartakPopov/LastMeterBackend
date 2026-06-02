package org.example.lastmeterbackend.DAL.mappers;

import org.example.lastmeterbackend.DAL.entities.BuildingEntity;
import org.example.lastmeterbackend.DAL.entities.LockerEntity;
import org.example.lastmeterbackend.domain.models.Building;
import org.example.lastmeterbackend.domain.models.Locker;
import org.springframework.stereotype.Component;

@Component
public class LockerPersistenceMapper {

    public Locker toDomain(LockerEntity entity) {
        if (entity == null) return null;
        return Locker.builder()
                .id(entity.getId())
                .lockerNumber(entity.getLockerNumber())
                .size(entity.getSize())
                .status(entity.getStatus())
                .building(mapBuilding(entity.getBuilding()))
                .build();
    }

    private Building mapBuilding(BuildingEntity entity) {
        if (entity == null) return null;
        return Building.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .build();
    }
}
