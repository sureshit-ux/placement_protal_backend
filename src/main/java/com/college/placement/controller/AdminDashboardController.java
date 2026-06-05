package com.college.placement.controller;
import com.college.placement.dto.response.AdminDashboardResponse;
import com.college.placement.service.AdminDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    @RequestMapping("/api/admin/dashboard")
    @RequiredArgsConstructor
    public class AdminDashboardController {

        private final AdminDashboardService adminDashboardService;

        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<AdminDashboardResponse> getDashboard() {

            return ResponseEntity.ok(
                    adminDashboardService.getDashboard()
            );
        }
    }


