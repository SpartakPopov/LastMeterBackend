package org.example.lastmeterbackend.DAL.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "package_id", nullable = false)
    private PackageEntity packageEntity;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false, length = 100)
    private String type;

    @Column(length = 255)
    private String packageDetailsUrl;

    @Column(length = 1000)
    private String pickupInstructions;

    @Column(name = "read_flag", nullable = false)
    private boolean read;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
