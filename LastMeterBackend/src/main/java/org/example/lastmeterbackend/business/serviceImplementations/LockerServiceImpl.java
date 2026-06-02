package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.business.services.LockerService;
import org.example.lastmeterbackend.domain.models.Locker;
import org.example.lastmeterbackend.domain.repositories.LockerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LockerServiceImpl implements LockerService {

    private final LockerRepository lockerRepository;

    public LockerServiceImpl(LockerRepository lockerRepository) {
        this.lockerRepository = lockerRepository;
    }

    @Override
    public List<Locker> getAllLockers() {
        return lockerRepository.findAll();
    }
}
