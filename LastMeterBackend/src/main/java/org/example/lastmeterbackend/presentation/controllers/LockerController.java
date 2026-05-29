package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.DAL.repositories.LockerJpaRepository;
import org.example.lastmeterbackend.presentation.dtos.LockerResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lockers")
@CrossOrigin(origins = "http://localhost:5173")
public class LockerController {

    private final LockerJpaRepository lockerJpaRepository;

    public LockerController(LockerJpaRepository lockerJpaRepository) {
        this.lockerJpaRepository = lockerJpaRepository;
    }

    @GetMapping("/all")
    public List<LockerResponseDto> getAllLockers() {
        return lockerJpaRepository.findAll().stream()
                .map(l -> LockerResponseDto.builder()
                        .id(l.getId())
                        .lockerNumber(l.getLockerNumber())
                        .size(l.getSize().name())
                        .status(l.getStatus().name())
                        .buildingName(l.getBuilding().getName())
                        .buildingAddress(l.getBuilding().getAddress())
                        .build())
                .collect(Collectors.toList());
    }
}
