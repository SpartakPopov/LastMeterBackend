package org.example.lastmeterbackend.exceptions;

import org.example.lastmeterbackend.domain.enums.PackageStatus;

public class PackageStateConflictException extends RuntimeException {

    private final PackageStatus currentStatus;

    public PackageStateConflictException(PackageStatus currentStatus) {
        super("Package cannot be picked up: current status is " + currentStatus);
        this.currentStatus = currentStatus;
    }

    public PackageStateConflictException(String message, PackageStatus currentStatus) {
        super(message);
        this.currentStatus = currentStatus;
    }

    public PackageStatus getCurrentStatus() {
        return currentStatus;
    }
}
