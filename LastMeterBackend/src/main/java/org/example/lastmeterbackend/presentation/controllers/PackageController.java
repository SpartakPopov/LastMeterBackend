package org.example.lastmeterbackend.presentation.controllers;

import org.example.lastmeterbackend.business.services.PackageService;
import org.example.lastmeterbackend.domain.models.Package;
import org.example.lastmeterbackend.presentation.dtos.PackageResponseDto;
import org.example.lastmeterbackend.presentation.mappers.PackageResponseDtoMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/packages") //had to add api before as vite automatically connects those urls
public class PackageController {

    private final PackageService packageService;
    private final PackageResponseDtoMapper packageResponseDtoMapper;

    public PackageController(PackageService packageService,
                             PackageResponseDtoMapper packageResponseDtoMapper) {
        this.packageService = packageService;
        this.packageResponseDtoMapper = packageResponseDtoMapper;
    }

    @GetMapping("/{trackingNumber}")
    public PackageResponseDto getByTrackingNumber(@PathVariable String trackingNumber) {
        Package pkg = packageService.getByTrackingNumber(trackingNumber);
        return packageResponseDtoMapper.toDto(pkg);
    }
}