package com.studentpractice.portfoliobackend.repository;

import com.studentpractice.portfoliobackend.model.VisitEntity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<VisitEntity, Long> {
    long countByVisitedTimeAfter(LocalDateTime since);
}
