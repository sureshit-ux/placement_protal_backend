package com.college.placement.repository;

import com.college.placement.entity.Branch;
import com.college.placement.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    // Find upcoming drives
    Page<Company> findByDriveDateAfter(LocalDateTime date, Pageable pageable);
    
    // Find companies eligible for a student based on branch, year, cgpa
    @Query("SELECT DISTINCT c FROM Company c " +
           "JOIN c.allowedBranches b " +
           "JOIN c.allowedYears y " +
           "WHERE b = :branch " +
           "AND y = :year " +
           "AND c.minimumCgpa <= :cgpa " +
           "AND c.applyDeadline >= :currentDate")
    Page<Company> findEligibleCompaniesForStudent(@Param("branch") Branch branch, 
                                                  @Param("year") Integer year, 
                                                  @Param("cgpa") Double cgpa, 
                                                  @Param("currentDate") LocalDateTime currentDate, 
                                                  Pageable pageable);
}
