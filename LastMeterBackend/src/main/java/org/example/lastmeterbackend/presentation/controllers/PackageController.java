package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.presentation.dtos.PackageCreationDto;
import org.example.lastmeterbackend.presentation.dtos.PackageResponseDto;
import org.example.lastmeterbackend.presentation.mappers.PackageDtoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.example.lastmeterbackend.presentation.mappers.PackageResponseDtoMapper;
import org.springframework.web.bind.annotation.*;

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
//Create a package with all the package info
    @PostMapping("/{create}")
    public PackageCreationDto createPackage(@RequestBody Package pkg) {
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
}