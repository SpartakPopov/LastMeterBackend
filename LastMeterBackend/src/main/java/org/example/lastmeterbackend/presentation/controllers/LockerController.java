package org.example.lastmeterbackend.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lastmeterbackend.business.services.LockerService;
import org.example.lastmeterbackend.presentation.dtos.LockerResponseDto;
import org.example.lastmeterbackend.presentation.mappers.LockerDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lockers")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Lockers", description = "Locker inventory — retrieve available and occupied lockers across all buildings")
public class LockerController {

    private final LockerService lockerService;
    private final LockerDtoMapper lockerDtoMapper;

    public LockerController(LockerService lockerService, LockerDtoMapper lockerDtoMapper) {
        this.lockerService = lockerService;
        this.lockerDtoMapper = lockerDtoMapper;
    }

    @Operation(summary = "Get all lockers", description = "Returns every locker in the system with its current status (AVAILABLE, OCCUPIED, OUT_OF_SERVICE), size, and building.")
    @ApiResponse(responseCode = "200", description = "List of all lockers")
    @GetMapping("/all")
    public List<LockerResponseDto> getAllLockers() {
        return lockerService.getAllLockers().stream()
                .map(lockerDtoMapper::toDto)
                .toList();
    }
}
