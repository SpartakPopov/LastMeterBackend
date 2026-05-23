package org.example.lastmeterbackend.domain.repositories;

import org.example.lastmeterbackend.domain.models.OrderGroup;

import java.util.List;
import java.util.Optional;

public interface OrderGroupRepository {
    OrderGroup save(OrderGroup orderGroup);
    Optional<OrderGroup> findById(Long id);
    List<OrderGroup> findAll();
    void deleteById(Long id);
}
