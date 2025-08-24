package com.linkedin.backend.features.learningplane.repository;

import com.linkedin.backend.features.learningplane.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // Custom queries if needed
} 