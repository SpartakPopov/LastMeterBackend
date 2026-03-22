package org.example.lastmeterbackend.domain.models;

import lombok.*;
import org.example.lastmeterbackend.domain.enums.PackageStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Package {

    @Setter(AccessLevel.NONE)
    private Long id;

    private String trackingNumber;
    private String description;

    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;

    private PackageStatus status;

    private User receiver;
    private Locker locker;

    private LocalDateTime deliveredAt;
    private LocalDateTime pickedUpAt;
}
