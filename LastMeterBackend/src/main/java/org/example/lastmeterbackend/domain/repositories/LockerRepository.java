package org.example.lastmeterbackend.domain.repositories;

import org.example.lastmeterbackend.domain.models.Locker;

import java.util.List;

public interface LockerRepository {
    List<Locker> findAll();
}
