package org.example.lastmeterbackend.business.services;
import org.example.lastmeterbackend.domain.models.Package;
public interface PackageService {
    Package getByTrackingNumber(String trackingNumber);
}
