package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.enums.PackageStatus;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.domain.models.User;
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

    public PackageController(PackageService packageService,
                             PackageDtoMapper packageDtoMapper) {
        this.packageService = packageService;
        this.packageDtoMapper = packageDtoMapper;
    }
    @PostMapping("/create")
    public PackageCreationDto createPackage(@RequestBody PackageCreationDto dto) {
        User receiver = dto.getReceiverId() != null
                ? User.builder().id(dto.getReceiverId()).build()
                : null;

        Package pkg = Package.builder()
                .trackingNumber(dto.getTrackingNumber())
                .description(dto.getDescription())
                .length(dto.getLength())
                .width(dto.getWidth())
                .height(dto.getHeight())
                .status(dto.getStatus() != null ? dto.getStatus() : PackageStatus.PENDING)
                .receiver(receiver)
                .build();

        Package createdPkg = packageService.createPackage(pkg);
        return packageDtoMapper.toCreationDto(createdPkg);
    }

    @GetMapping("/{trackingNumber}")
    public PackageResponseDto getByTrackingNumber(@PathVariable String trackingNumber) {
        Package pkg = packageService.getByTrackingNumber(trackingNumber);
        return packageDtoMapper.toDto(pkg);
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
        Package fields = Package.builder()
                .trackingNumber(dto.getTrackingNumber())
                .description(dto.getDescription())
                .length(dto.getLength())
                .width(dto.getWidth())
                .height(dto.getHeight())
                .build();
        return packageDtoMapper.toDto(packageService.updatePackage(id, fields));
    }

    @PostMapping("/pickup")
    public PackageResponseDto pickupPackage(@RequestBody PickupRequestDto dto) {
        Package pkg = packageService.pickup(dto.getTrackingNumber());
        return packageDtoMapper.toDto(pkg);
    }
}