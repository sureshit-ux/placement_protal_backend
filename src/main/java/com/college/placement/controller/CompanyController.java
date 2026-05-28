package com.college.placement.controller;

import com.college.placement.dto.request.CompanyRequest;
import com.college.placement.dto.response.ApiResponse;
import com.college.placement.dto.response.CompanyResponse;
import com.college.placement.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller providing APIs for Company recruitment drives.
 * Features validated inputs, standard wrappers, and role protection boundaries.
 */
@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyRepositoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_COORDINATOR', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(@Valid @RequestBody CompanyRequest request) {
        CompanyResponse response = companyRepositoryService.createCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Recruitment drive created successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_COORDINATOR', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
            @PathVariable Long id, 
            @Valid @RequestBody CompanyRequest request) {
        CompanyResponse response = companyRepositoryService.updateCompany(id, request);
        return ResponseEntity.ok(ApiResponse.success("Recruitment drive updated successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_COORDINATOR', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable Long id) {
        CompanyResponse response = companyRepositoryService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponse.success("Recruitment drive retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_COORDINATOR', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies() {
        List<CompanyResponse> responseList = companyRepositoryService.getAllCompanies();
        return ResponseEntity.ok(ApiResponse.success("Active recruitment drives retrieved successfully", responseList));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_COORDINATOR', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        companyRepositoryService.deleteCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Recruitment drive deleted successfully", null));
    }
}
