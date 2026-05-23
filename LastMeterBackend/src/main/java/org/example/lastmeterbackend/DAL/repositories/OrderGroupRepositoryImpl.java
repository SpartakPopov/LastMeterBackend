package org.example.lastmeterbackend.DAL.repositories;

import org.example.lastmeterbackend.DAL.entities.OrderGroupEntity;
import org.example.lastmeterbackend.DAL.entities.OrderRequestEntity;
import org.example.lastmeterbackend.DAL.mappers.OrderGroupPersistenceMapper;
import org.example.lastmeterbackend.domain.models.OrderGroup;
import org.example.lastmeterbackend.domain.repositories.OrderGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderGroupRepositoryImpl implements OrderGroupRepository {

    private final OrderGroupJpaRepository jpaRepository;
    private final OrderRequestJpaRepository orderRequestJpaRepository;
    private final OrderGroupPersistenceMapper mapper;

    public OrderGroupRepositoryImpl(OrderGroupJpaRepository jpaRepository,
                                    OrderRequestJpaRepository orderRequestJpaRepository,
                                    OrderGroupPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.orderRequestJpaRepository = orderRequestJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderGroup save(OrderGroup orderGroup) {
        OrderGroupEntity entity = mapper.toEntity(orderGroup);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<OrderGroup> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrderGroup> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        OrderGroupEntity group = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderGroup not found with id: " + id));
        for (OrderRequestEntity req : group.getOrderRequests()) {
            req.setGroup(null);
            orderRequestJpaRepository.save(req);
        }
        jpaRepository.delete(group);
    }
}
