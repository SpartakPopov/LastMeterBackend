package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.mappers.LockerPersistenceMapper;
import org.example.lastmeterbackend.domain.models.Locker;
import org.example.lastmeterbackend.domain.repositories.LockerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LockerRepositoryImpl implements LockerRepository {

    private final LockerJpaRepository jpaRepository;
    private final LockerPersistenceMapper mapper;

    public LockerRepositoryImpl(LockerJpaRepository jpaRepository, LockerPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Locker> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
