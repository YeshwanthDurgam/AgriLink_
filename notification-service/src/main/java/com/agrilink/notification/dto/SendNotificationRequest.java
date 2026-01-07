package com.agrilink.notification.dto;

import com.agrilink.notification.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

/**
 * Request DTO for sending a notification.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    private String email;

    private String phone;

    @NotNull(message = "Notification type is required")
    private Notification.NotificationType notificationType;

    @NotNull(message = "Channel is required")
    private Notification.Channel channel;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    private Map<String, Object> data;
}
