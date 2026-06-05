package com.college.placement.controller;

import com.college.placement.dto.response.NotificationResponse;
import com.college.placement.security.UserPrincipal;
import com.college.placement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get all notifications for logged-in user
     *
     * GET /api/notifications
     */
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(
            Pageable pageable) {

        return ResponseEntity.ok(notificationService.getNotifications(pageable));
    }

    /**
     * Get unread notification count
     *
     * GET /api/notifications/unread-count
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        long count = notificationService.getUnreadCount(userPrincipal.getId());

        return ResponseEntity.ok(count);
    }

    /**
     * Mark notification as read
     *
     * PUT /api/notifications/{notificationId}/read
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId) {

        notificationService.markAsRead(notificationId);

        return ResponseEntity.ok().build();
    }
}