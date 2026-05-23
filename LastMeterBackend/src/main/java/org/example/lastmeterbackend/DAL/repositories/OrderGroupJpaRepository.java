package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.OrderGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderGroupJpaRepository extends JpaRepository<OrderGroupEntity, Long> {
}
