package org.example.lastmeterbackend.exceptions;

public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException(String trackingNumber) {
        super("Package not found with tracking number: " + trackingNumber);
    }
}
