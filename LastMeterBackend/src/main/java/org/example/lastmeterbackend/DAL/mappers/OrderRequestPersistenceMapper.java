package org.example.lastmeterbackend.DAL.mappers;

import org.example.lastmeterbackend.DAL.entities.OrderRequestEntity;
import org.example.lastmeterbackend.DAL.entities.UserEntity;
import org.example.lastmeterbackend.domain.models.OrderRequest;
import org.example.lastmeterbackend.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class OrderRequestPersistenceMapper {

    public OrderRequest toDomain(OrderRequestEntity entity) {
        if (entity == null) return null;
        return OrderRequest.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .productLinks(entity.getProductLinks())
                .quantity(entity.getQuantity())
                .requestedBy(mapUserToDomain(entity.getRequestedBy()))
                .requestedFor(mapUserToDomain(entity.getRequestedFor()))
                .status(entity.getStatus())
                .managerNotes(entity.getManagerNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public OrderRequestEntity toEntity(OrderRequest domain) {
        if (domain == null) return null;
        return OrderRequestEntity.builder()
                .id(domain.getId())
                .description(domain.getDescription())
                .productLinks(domain.getProductLinks())
                .quantity(domain.getQuantity())
                .requestedBy(mapUserToEntity(domain.getRequestedBy()))
                .requestedFor(mapUserToEntity(domain.getRequestedFor()))
                .status(domain.getStatus())
                .managerNotes(domain.getManagerNotes())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
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
