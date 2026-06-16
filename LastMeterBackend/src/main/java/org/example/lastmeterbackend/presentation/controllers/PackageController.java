package org.example.lastmeterbackend.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.presentation.dtos.DeliverRequestDto;
import org.example.lastmeterbackend.presentation.dtos.PackageCreationDto;
import org.example.lastmeterbackend.presentation.dtos.PackageResponseDto;
import org.example.lastmeterbackend.presentation.dtos.PackageUpdateDto;
import org.example.lastmeterbackend.presentation.dtos.PickupRequestDto;
import org.example.lastmeterbackend.presentation.mappers.PackageDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/packages")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Packages", description = "Package lifecycle management — create, track, deliver, and pick up packages")
public class PackageController {

    private final PackageService packageService;
    private final PackageDtoMapper packageDtoMapper;

    public PackageController(PackageService packageService, PackageDtoMapper packageDtoMapper) {
        this.packageService = packageService;
        this.packageDtoMapper = packageDtoMapper;
    }

    @Operation(summary = "Create a new package", description = "Registers a new incoming package. Receiver is optional — can be assigned later via update.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Package created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/create")
    public PackageCreationDto createPackage(@RequestBody PackageCreationDto dto) {
        Package createdPkg = packageService.createPackage(
                dto.getTrackingNumber(), dto.getDescription(),
                dto.getLength(), dto.getWidth(), dto.getHeight(),
                dto.getStatus(), dto.getReceiverId()
        );
        return packageDtoMapper.toCreationDto(createdPkg);
    }

    @Operation(summary = "Get package by tracking number", description = "Returns full package details including locker and receiver information.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Package found"),
        @ApiResponse(responseCode = "404", description = "Package not found")
    })
    @GetMapping("/{trackingNumber}")
    public PackageResponseDto getByTrackingNumber(
            @Parameter(description = "Unique tracking number of the package") @PathVariable String trackingNumber) {
        return packageDtoMapper.toDto(packageService.getByTrackingNumber(trackingNumber));
    }

    @Operation(summary = "Get all packages", description = "Returns every package in the system regardless of status.")
    @ApiResponse(responseCode = "200", description = "List of all packages")
    @GetMapping("/all")
    public List<PackageResponseDto> getAllPackages() {
        return packageService.getAllPackages().stream()
                .map(packageDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get packages by receiver", description = "Returns all packages assigned to a specific user (receiver).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of packages for the receiver"),
        @ApiResponse(responseCode = "404", description = "Receiver not found")
    })
    @GetMapping("/receiver/{receiverId}")
    public List<PackageResponseDto> getAllPackagesByReceiver(
            @Parameter(description = "ID of the receiving user") @PathVariable Long receiverId) {
        return packageService.getAllPackagesByReceiver(receiverId).stream()
                .map(packageDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get unassigned packages", description = "Returns packages that have no locker assigned yet (status PENDING).")
    @ApiResponse(responseCode = "200", description = "List of unassigned packages")
    @GetMapping("/unassigned")
    public List<PackageResponseDto> getUnassignedPackages() {
        return packageService.getUnassignedPackages().stream()
                .map(packageDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Update a package", description = "Updates package details such as tracking number, dimensions, status, or assigned locker.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Package updated successfully"),
        @ApiResponse(responseCode = "404", description = "Package not found"),
        @ApiResponse(responseCode = "409", description = "Invalid status transition")
    })
    @PutMapping("/{id}")
    public PackageResponseDto updatePackage(
            @Parameter(description = "Internal ID of the package") @PathVariable Long id,
            @RequestBody PackageUpdateDto dto) {
        Package updated = packageService.updatePackage(
                id, dto.getTrackingNumber(), dto.getDescription(),
                dto.getLength(), dto.getWidth(), dto.getHeight(),
                dto.getStatus(), dto.getLockerId()
        );
        return packageDtoMapper.toDto(updated);
    }

    @Operation(summary = "Record package pickup", description = "Marks a package as PICKED_UP and frees the assigned locker. Package must be in DELIVERED_TO_LOCKER status.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Package picked up, locker released"),
        @ApiResponse(responseCode = "404", description = "Package not found"),
        @ApiResponse(responseCode = "409", description = "Package not in a pickup-eligible status")
    })
    @PostMapping("/pickup")
    public PackageResponseDto pickupPackage(@RequestBody PickupRequestDto dto) {
        return packageDtoMapper.toDto(packageService.pickup(dto.getTrackingNumber()));
    }

    @Operation(summary = "Mark package as delivered to locker", description = "Transitions status to DELIVERED_TO_LOCKER and triggers a notification to the receiver.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Package delivered, notification sent"),
        @ApiResponse(responseCode = "404", description = "Package not found"),
        @ApiResponse(responseCode = "409", description = "Package not in ASSIGNED_TO_LOCKER status")
    })
    @PostMapping("/deliver")
    public PackageResponseDto deliverPackage(@RequestBody DeliverRequestDto dto) {
        return packageDtoMapper.toDto(packageService.deliver(dto.getTrackingNumber()));
    }
}
