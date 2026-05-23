package org.example.lastmeterbackend.DAL.mappers;

import org.example.lastmeterbackend.DAL.entities.OrderGroupEntity;
import org.example.lastmeterbackend.DAL.entities.UserEntity;
import org.example.lastmeterbackend.domain.models.OrderGroup;
import org.example.lastmeterbackend.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class OrderGroupPersistenceMapper {

    private final OrderRequestPersistenceMapper orderRequestMapper;

    public OrderGroupPersistenceMapper(OrderRequestPersistenceMapper orderRequestMapper) {
        this.orderRequestMapper = orderRequestMapper;
    }

    public OrderGroup toDomain(OrderGroupEntity entity) {
        if (entity == null) return null;
        return OrderGroup.builder()
                .id(entity.getId())
                .name(entity.getName())
                .requestedBy(mapUserToDomain(entity.getRequestedBy()))
                .orderRequests(entity.getOrderRequests().stream()
                        .map(orderRequestMapper::toDomain)
                        .toList())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public OrderGroupEntity toEntity(OrderGroup domain) {
        if (domain == null) return null;
        return OrderGroupEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .requestedBy(mapUserToEntity(domain.getRequestedBy()))
                .createdAt(domain.getCreatedAt())
                .build();
    }

    private User mapUserToDomain(UserEntity entity) {
        if (entity == null) return null;
        return User.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }

    private UserEntity mapUserToEntity(User domain) {
        if (domain == null) return null;
        return UserEntity.builder()
                .id(domain.getId())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .email(domain.getEmail())
                .role(domain.getRole())
                .build();
    }
}
