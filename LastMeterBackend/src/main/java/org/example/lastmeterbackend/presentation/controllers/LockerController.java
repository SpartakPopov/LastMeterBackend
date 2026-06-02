package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.business.services.LockerService;
import org.example.lastmeterbackend.presentation.dtos.LockerResponseDto;
import org.example.lastmeterbackend.presentation.mappers.LockerDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lockers")
@CrossOrigin(origins = "http://localhost:5173")
public class LockerController {

    private final LockerService lockerService;
    private final LockerDtoMapper lockerDtoMapper;

    public LockerController(LockerService lockerService, LockerDtoMapper lockerDtoMapper) {
        this.lockerService = lockerService;
        this.lockerDtoMapper = lockerDtoMapper;
    }

    @GetMapping("/all")
    public List<LockerResponseDto> getAllLockers() {
        return lockerService.getAllLockers().stream()
                .map(lockerDtoMapper::toDto)
                .toList();
    }
}
