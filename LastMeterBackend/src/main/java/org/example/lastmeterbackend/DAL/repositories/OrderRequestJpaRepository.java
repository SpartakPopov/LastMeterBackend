package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.OrderRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRequestJpaRepository extends JpaRepository<OrderRequestEntity, Long> {
    List<OrderRequestEntity> findByRequestedById(Long requestedById);
}
