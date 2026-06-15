package org.example.lastmeterbackend.presentation.controllers;

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
public class PackageController {

    private final PackageService packageService;
    private final PackageDtoMapper packageDtoMapper;

    public PackageController(PackageService packageService, PackageDtoMapper packageDtoMapper) {
        this.packageService = packageService;
        this.packageDtoMapper = packageDtoMapper;
    }

    @PostMapping("/create")
    public PackageCreationDto createPackage(@RequestBody PackageCreationDto dto) {
        Package createdPkg = packageService.createPackage(
                dto.getTrackingNumber(), dto.getDescription(),
                dto.getLength(), dto.getWidth(), dto.getHeight(),
                dto.getStatus(), dto.getReceiverId()
        );
        return packageDtoMapper.toCreationDto(createdPkg);
    }

    @GetMapping("/{trackingNumber}")
    public PackageResponseDto getByTrackingNumber(@PathVariable String trackingNumber) {
        return packageDtoMapper.toDto(packageService.getByTrackingNumber(trackingNumber));
    }

    @GetMapping("/all")
    public List<PackageResponseDto> getAllPackages() {
        return packageService.getAllPackages().stream()
                .map(packageDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/receiver/{receiverId}")
    public List<PackageResponseDto> getAllPackagesByReceiver(@PathVariable Long receiverId) {
        return packageService.getAllPackagesByReceiver(receiverId).stream()
                .map(packageDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/unassigned")
    public List<PackageResponseDto> getUnassignedPackages() {
        return packageService.getUnassignedPackages().stream()
                .map(packageDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public PackageResponseDto updatePackage(@PathVariable Long id, @RequestBody PackageUpdateDto dto) {
        Package updated = packageService.updatePackage(
                id, dto.getTrackingNumber(), dto.getDescription(),
                dto.getLength(), dto.getWidth(), dto.getHeight(),
                dto.getStatus(), dto.getLockerId()
        );
        return packageDtoMapper.toDto(updated);
    }

    @PostMapping("/pickup")
    public PackageResponseDto pickupPackage(@RequestBody PickupRequestDto dto) {
        return packageDtoMapper.toDto(packageService.pickup(dto.getTrackingNumber()));
    }

    @PostMapping("/deliver")
    public PackageResponseDto deliverPackage(@RequestBody DeliverRequestDto dto) {
        return packageDtoMapper.toDto(packageService.deliver(dto.getTrackingNumber()));
    }
}
