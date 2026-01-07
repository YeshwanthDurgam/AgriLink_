package com.agrilink.notification.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.common.dto.PagedResponse;
import com.agrilink.notification.dto.*;
import com.agrilink.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for notification operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Send a notification (internal service endpoint).
     * POST /api/v1/notifications/send
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<NotificationDto>> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        NotificationDto notification = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification sent", notification));
    }

    /**
     * Send notification using template (internal service endpoint).
     * POST /api/v1/notifications/send/template
     */
    @PostMapping("/send/template")
    public ResponseEntity<ApiResponse<NotificationDto>> sendTemplateNotification(
            @Valid @RequestBody SendTemplateNotificationRequest request) {
        NotificationDto notification = notificationService.sendTemplateNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification sent", notification));
    }

    /**
     * Get my notifications.
     * GET /api/v1/notifications
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<NotificationDto>>> getMyNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<NotificationDto> notifications = notificationService.getUserNotifications(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(notifications)));
    }

    /**
     * Get unread notifications.
     * GET /api/v1/notifications/unread
     */
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getUnreadNotifications(Authentication authentication) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        List<NotificationDto> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    /**
     * Get unread count.
     * GET /api/v1/notifications/count
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(Authentication authentication) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * Mark notification as read.
     * POST /api/v1/notifications/{notificationId}/read
     */
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<NotificationDto>> markAsRead(
            Authentication authentication,
            @PathVariable UUID notificationId) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        NotificationDto notification = notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", notification));
    }

    /**
     * Mark all notifications as read.
     * POST /api/v1/notifications/read-all
     */
    @PostMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(Authentication authentication) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read"));
    }

    /**
     * Get notification preferences.
     * GET /api/v1/notifications/preferences
     */
    @GetMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferencesDto>> getPreferences(Authentication authentication) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        NotificationPreferencesDto preferences = notificationService.getPreferences(userId);
        return ResponseEntity.ok(ApiResponse.success(preferences));
    }

    /**
     * Update notification preferences.
     * PUT /api/v1/notifications/preferences
     */
    @PutMapping("/preferences")
    public ResponseEntity<ApiResponse<NotificationPreferencesDto>> updatePreferences(
            Authentication authentication,
            @RequestBody UpdatePreferencesRequest request) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        NotificationPreferencesDto preferences = notificationService.updatePreferences(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Preferences updated", preferences));
    }
}
