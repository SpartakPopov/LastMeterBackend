package org.example.lastmeterbackend.business.serviceImplementations;

import org.example.lastmeterbackend.DAL.entities.OrderGroupEntity;
import org.example.lastmeterbackend.DAL.entities.OrderRequestEntity;
import org.example.lastmeterbackend.DAL.entities.UserEntity;
import org.example.lastmeterbackend.DAL.repositories.OrderGroupJpaRepository;
import org.example.lastmeterbackend.DAL.repositories.OrderRequestJpaRepository;
import org.example.lastmeterbackend.DAL.mappers.OrderGroupPersistenceMapper;
import org.example.lastmeterbackend.business.services.OrderGroupService;
import org.example.lastmeterbackend.domain.models.OrderGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderGroupServiceImpl implements OrderGroupService {

    private final OrderGroupJpaRepository groupJpaRepository;
    private final OrderRequestJpaRepository orderRequestJpaRepository;
    private final OrderGroupPersistenceMapper mapper;

    public OrderGroupServiceImpl(OrderGroupJpaRepository groupJpaRepository,
                                 OrderRequestJpaRepository orderRequestJpaRepository,
                                 OrderGroupPersistenceMapper mapper) {
        this.groupJpaRepository = groupJpaRepository;
        this.orderRequestJpaRepository = orderRequestJpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public OrderGroup createGroup(String name, Long requestedById, List<Long> orderRequestIds) {
        List<OrderRequestEntity> requests = orderRequestJpaRepository.findAllById(orderRequestIds);

        boolean allSameUser = requests.stream()
                .allMatch(r -> r.getRequestedBy().getId().equals(requestedById));
        if (!allSameUser) {
            throw new IllegalArgumentException("All order requests must belong to the same user");
        }

        OrderGroupEntity group = OrderGroupEntity.builder()
                .name(name)
                .requestedBy(UserEntity.builder().id(requestedById).build())
                .createdAt(LocalDateTime.now())
                .build();

        OrderGroupEntity saved = groupJpaRepository.save(group);

        for (OrderRequestEntity req : requests) {
            req.setGroup(saved);
            orderRequestJpaRepository.save(req);
        }

        return mapper.toDomain(groupJpaRepository.findById(saved.getId()).orElseThrow());
    }

    @Override
    public List<OrderGroup> getAllGroups() {
        return groupJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public OrderGroup getGroupById(Long id) {
        return groupJpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("OrderGroup not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteGroup(Long id) {
        OrderGroupEntity group = groupJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderGroup not found with id: " + id));
        for (OrderRequestEntity req : group.getOrderRequests()) {
            req.setGroup(null);
            orderRequestJpaRepository.save(req);
        }
        groupJpaRepository.delete(group);
    }
}
