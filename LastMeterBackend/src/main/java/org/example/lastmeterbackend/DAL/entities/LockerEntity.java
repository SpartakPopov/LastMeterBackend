package org.example.lastmeterbackend.DAL.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.lastmeterbackend.domain.enums.LockerSize;
import org.example.lastmeterbackend.domain.enums.LockerStatus;

@Entity
@Table(name = "lockers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LockerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String lockerNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LockerSize size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private LockerStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private BuildingEntity building;
}
