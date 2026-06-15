package org.example.lastmeterbackend.exceptions;

public class NoLockerAvailableException extends RuntimeException {
    public NoLockerAvailableException() {
        super("No available locker to deliver the package.");
    }
}
