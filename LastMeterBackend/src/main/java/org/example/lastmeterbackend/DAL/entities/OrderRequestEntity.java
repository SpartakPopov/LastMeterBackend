package org.example.lastmeterbackend.DAL.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.lastmeterbackend.domain.enums.OrderRequestStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_requests")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(length = 1000)
    private String description;

    @Column(length = 2000)
    private String productLinks;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_by_id")
    private UserEntity requestedBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_for_id")
    private UserEntity requestedFor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderRequestStatus status;

    @Column(length = 1000)
    private String managerNotes;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private OrderGroupEntity group;
}
