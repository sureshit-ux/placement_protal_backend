package com.college.placement.repository;

import com.college.placement.entity.CoordinatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoordinatorProfileRepository extends JpaRepository<CoordinatorProfile, Long> {
    Optional<CoordinatorProfile> findByUserId(Long userId);
}
