package com.agrilink.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

/**
 * Request DTO for sending notification using a template.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendTemplateNotificationRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    private String email;

    private String phone;

    @NotBlank(message = "Template code is required")
    private String templateCode;

    private Map<String, String> variables;
}
