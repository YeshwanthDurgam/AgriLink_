package com.agrilink.notification.dto;

import com.agrilink.notification.entity.Notification;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for Notification.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private UUID id;
    private UUID userId;
    private Notification.NotificationType notificationType;
    private Notification.Channel channel;
    private String title;
    private String message;
    private Map<String, Object> data;
    private Notification.Status status;
    private boolean read;
    private LocalDateTime readAt;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}
