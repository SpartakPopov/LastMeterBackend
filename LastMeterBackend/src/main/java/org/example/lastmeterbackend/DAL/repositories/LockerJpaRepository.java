package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.LockerEntity;
import org.example.lastmeterbackend.domain.enums.LockerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LockerJpaRepository extends JpaRepository<LockerEntity, Long> {
    Optional<LockerEntity> findFirstByStatus(LockerStatus status);
}
