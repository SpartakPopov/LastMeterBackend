package org.example.lastmeterbackend.domain.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long userId;
    private Long packageId;
    private String trackingNumber;
    private String title;
    private String message;
    private String type;
    private String packageDetailsUrl;
    private String pickupInstructions;
    private boolean read;
    private LocalDateTime createdAt;
}
