package org.example.lastmeterbackend.DAL.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.lastmeterbackend.domain.enums.PackageStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String trackingNumber;

    @Column(length = 500)
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal length;

    @Column(precision = 10, scale = 2)
    private BigDecimal width;

    @Column(precision = 10, scale = 2)
    private BigDecimal height;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PackageStatus status;

    @ManyToOne(optional = true)
    @JoinColumn(name = "receiver_id", nullable = true)
    private UserEntity receiver;

    @ManyToOne
    @JoinColumn(name = "locker_id")
    private LockerEntity locker;

    @Column
    private LocalDateTime deliveredAt;

    @Column
    private LocalDateTime pickedUpAt;
}
