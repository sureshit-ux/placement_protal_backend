package com.college.placement.service;

import com.college.placement.dto.request.CompanyRequest;
import com.college.placement.dto.response.CompanyResponse;
import java.util.List;

/**
 * Service interface defining recruitment drive operations.
 */
public interface CompanyService {

    CompanyResponse createCompany(CompanyRequest request);

    CompanyResponse updateCompany(Long id, CompanyRequest request);

    CompanyResponse getCompanyById(Long id);

    List<CompanyResponse> getAllCompanies();

    void deleteCompany(Long id);
}
