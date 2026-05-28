package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.OrderRequestEntity;
import org.example.lastmeterbackend.DAL.mappers.OrderRequestPersistenceMapper;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;
import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.domain.repositories.OrderRequestRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRequestRepositoryImpl implements OrderRequestRepository {

    private final OrderRequestJpaRepository jpaRepository;
    private final OrderRequestPersistenceMapper mapper;

    public OrderRequestRepositoryImpl(OrderRequestJpaRepository jpaRepository,
                                      OrderRequestPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderRequest save(OrderRequest orderRequest) {
        OrderRequestEntity entity = mapper.toEntity(orderRequest);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<OrderRequest> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrderRequest> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<OrderRequest> findByRequestedById(Long userId) {
        return jpaRepository.findByRequestedById(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public OrderRequest updateStatus(Long id, OrderRequestStatus status, String managerNotes) {
        OrderRequestEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderRequest not found with id: " + id));
        entity.setStatus(status);
        entity.setManagerNotes(managerNotes);
        entity.setUpdatedAt(LocalDateTime.now());
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public OrderRequest fulfill(Long id) {
        OrderRequestEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderRequest not found with id: " + id));
        entity.setStatus(OrderRequestStatus.ORDERED);
        entity.setUpdatedAt(LocalDateTime.now());
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public OrderRequest updateFields(Long id, String description, String productLinks, Integer quantity) {
        OrderRequestEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderRequest not found with id: " + id));
        if (description != null) entity.setDescription(description);
        if (productLinks != null) entity.setProductLinks(productLinks);
        if (quantity != null) entity.setQuantity(quantity);
        entity.setUpdatedAt(LocalDateTime.now());
        return mapper.toDomain(jpaRepository.save(entity));
    }
}
