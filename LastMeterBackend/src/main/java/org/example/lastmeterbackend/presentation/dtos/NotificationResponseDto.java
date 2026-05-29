package org.example.lastmeterbackend.presentation.dtos;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {
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
