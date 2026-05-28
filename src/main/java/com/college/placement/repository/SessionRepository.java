package com.college.placement.repository;

import com.college.placement.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Page<Session> findBySessionDateAfter(LocalDateTime date, Pageable pageable);
    Page<Session> findBySessionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
