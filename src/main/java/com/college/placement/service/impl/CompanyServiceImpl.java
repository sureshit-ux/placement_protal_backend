package com.college.placement.service.impl;

import com.college.placement.dto.request.CompanyRequest;
import com.college.placement.dto.response.BranchResponse;
import com.college.placement.dto.response.CompanyResponse;
import com.college.placement.entity.Branch;
import com.college.placement.entity.Company;
import com.college.placement.exception.BadRequestException;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.BranchRepository;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation managing Company drive records.
 * Incorporates transactional logic and DTO mapping.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;

    @Override
    @Transactional
    public CompanyResponse createCompany(CompanyRequest request) {
        validateDeadlines(request);

        // Fetch and map allowed branches
        Set<Branch> branches = fetchBranches(request.getAllowedBranchIds());

        Company company = Company.builder()
                .companyName(request.getCompanyName())
                .roleOffered(request.getRoleOffered())
                .packageOffered(request.getPackageOffered())
                .minimumCgpa(request.getMinimumCgpa())
                .backlogsAllowed(request.getBacklogsAllowed())
                .driveDate(request.getDriveDate())
                .applyDeadline(request.getApplyDeadline())
                .jobDescription(request.getJobDescription())
                .allowedBranches(branches)
                .allowedYears(request.getAllowedYears())
                .preparationResources(request.getPreparationResources())
                .build();

        Company savedCompany = companyRepository.save(company);
        return mapToResponse(savedCompany);
    }

    @Override
    @Transactional
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        validateDeadlines(request);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        Set<Branch> branches = fetchBranches(request.getAllowedBranchIds());

        company.setCompanyName(request.getCompanyName());
        company.setRoleOffered(request.getRoleOffered());
        company.setPackageOffered(request.getPackageOffered());
        company.setMinimumCgpa(request.getMinimumCgpa());
        company.setBacklogsAllowed(request.getBacklogsAllowed());
        company.setDriveDate(request.getDriveDate());
        company.setApplyDeadline(request.getApplyDeadline());
        company.setJobDescription(request.getJobDescription());
        company.setAllowedBranches(branches);
        company.setAllowedYears(request.getAllowedYears());

        // Update @ElementCollection directly
        company.getPreparationResources().clear();
        if (request.getPreparationResources() != null) {
            company.getPreparationResources().addAll(request.getPreparationResources());
        }

        Company updatedCompany = companyRepository.save(company);
        return mapToResponse(updatedCompany);
    }

    @Override
    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        return mapToResponse(company);
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found with ID: " + id);
        }
        companyRepository.deleteById(id);
    }

    // Helper methods
    private void validateDeadlines(CompanyRequest request) {
        if (request.getApplyDeadline().isAfter(request.getDriveDate())) {
            throw new BadRequestException("Apply deadline must be scheduled before the actual drive date.");
        }
    }

    private Set<Branch> fetchBranches(Set<Long> branchIds) {
        Set<Branch> branches = new HashSet<>();
        for (Long branchId : branchIds) {
            Branch branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with ID: " + branchId));
            branches.add(branch);
        }
        return branches;
    }

    private CompanyResponse mapToResponse(Company company) {
        Set<BranchResponse> branchResponses = company.getAllowedBranches().stream()
                .map(b -> BranchResponse.builder()
                        .id(b.getId())
                        .name(b.getName())
                        .code(b.getCode())
                        .department(b.getDepartment())
                        .build())
                .collect(Collectors.toSet());

        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .roleOffered(company.getRoleOffered())
                .packageOffered(company.getPackageOffered())
                .minimumCgpa(company.getMinimumCgpa())
                .backlogsAllowed(company.getBacklogsAllowed())
                .driveDate(company.getDriveDate())
                .applyDeadline(company.getApplyDeadline())
                .jobDescription(company.getJobDescription())
                .allowedBranches(branchResponses)
                .allowedYears(company.getAllowedYears())
                .preparationResources(new HashSet<>(company.getPreparationResources()))
                .build();
    }
}
