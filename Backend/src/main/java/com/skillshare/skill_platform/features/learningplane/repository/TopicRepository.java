package com.linkedin.backend.features.learningplane.repository;

import com.linkedin.backend.features.learningplane.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    // Custom queries if needed
} 