package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.DAL.entities.LockerEntity;
import org.example.lastmeterbackend.DAL.repositories.LockerJpaRepository;
import org.example.lastmeterbackend.presentation.dtos.LockerResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lockers")
public class LockerController {

    private final LockerJpaRepository lockerJpaRepository;

    public LockerController(LockerJpaRepository lockerJpaRepository) {
        this.lockerJpaRepository = lockerJpaRepository;
    }

    @GetMapping("/all")
    public List<LockerResponseDto> getAllLockers() {
        return lockerJpaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private LockerResponseDto toDto(LockerEntity l) {
        String buildingName = l.getBuilding() != null ? l.getBuilding().getName() : null;
        String buildingAddress = l.getBuilding() != null ? l.getBuilding().getAddress() : null;
        return LockerResponseDto.builder()
                .id(l.getId())
                .lockerNumber(l.getLockerNumber())
                .size(l.getSize() != null ? l.getSize().name() : null)
                .status(l.getStatus() != null ? l.getStatus().name() : null)
                .buildingName(buildingName)
                .buildingAddress(buildingAddress)
                .build();
    }
}
