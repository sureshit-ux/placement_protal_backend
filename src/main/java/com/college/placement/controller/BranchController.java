package com.college.placement.controller;

import com.college.placement.dto.request.BranchRequest;
import com.college.placement.dto.response.BranchResponse;
import com.college.placement.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller exposing academic Branch administration and catalog APIs.
 */
@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@Slf4j
public class BranchController {

    private final BranchService branchService;

    /**
     * Retrieves all academic branches registered in the placement database.
     * Accessible by students, coordinators, and administrators.
     *
     * @return ResponseEntity with the list of branches and HTTP 200 OK
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COORDINATOR', 'STUDENT')")
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        log.info("REST request to fetch all academic branches");
        List<BranchResponse> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    /**
     * Fetches detailed branch information by branch ID.
     *
     * @param id the branch ID
     * @return ResponseEntity with branch details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COORDINATOR', 'STUDENT')")
    public ResponseEntity<BranchResponse> getBranchById(@PathVariable("id") Long id) {
        log.info("REST request to fetch branch by ID: {}", id);
        BranchResponse response = branchService.getBranchById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Registers a new academic branch in the placement database.
     * Restricted to administrators only.
     *
     * @param request the branch details DTO
     * @return ResponseEntity with created BranchResponse and HTTP 201 Created
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponse> createBranch(@Valid @RequestBody BranchRequest request) {
        log.info("REST request to create branch with code: {}", request.getCode());
        BranchResponse response = branchService.createBranch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Updates an existing academic branch details.
     * Restricted to administrators only.
     *
     * @param id the branch ID to update
     * @param request the updated branch properties DTO
     * @return ResponseEntity with updated BranchResponse and HTTP 200 OK
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchResponse> updateBranch(
            @PathVariable("id") Long id,
            @Valid @RequestBody BranchRequest request) {
        log.info("REST request to update branch ID: {}", id);
        BranchResponse response = branchService.updateBranch(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes an academic branch from the database.
     * Restricted to administrators only.
     *
     * @param id the branch ID to delete
     * @return ResponseEntity with HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBranch(@PathVariable("id") Long id) {
        log.info("REST request to delete branch ID: {}", id);
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}
