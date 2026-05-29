package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.LockerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockerJpaRepository extends JpaRepository<LockerEntity, Long> {
}
