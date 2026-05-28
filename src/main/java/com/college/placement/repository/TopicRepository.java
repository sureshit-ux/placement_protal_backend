package com.college.placement.repository;

import com.college.placement.entity.Branch;
import com.college.placement.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    
    // Find all global topics
    Page<Topic> findByIsGlobalTrue(Pageable pageable);
    
    // Find topics applicable to a specific branch (including global topics)
    @Query("SELECT DISTINCT t FROM Topic t LEFT JOIN t.applicableBranches b " +
           "WHERE t.isGlobal = true OR b = :branch")
    Page<Topic> findTopicsForBranch(@Param("branch") Branch branch, Pageable pageable);
    
    // Find topics by category
    Page<Topic> findByCategory(String category, Pageable pageable);
}
