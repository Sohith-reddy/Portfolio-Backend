package com.studentpractice.portfoliobackend.repository;

import com.studentpractice.portfoliobackend.model.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<VisitEntity,Long> {
}
