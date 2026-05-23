package org.example.lastmeterbackend.DAL.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_groups")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_by_id")
    private UserEntity requestedBy;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    @Builder.Default
    private List<OrderRequestEntity> orderRequests = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
