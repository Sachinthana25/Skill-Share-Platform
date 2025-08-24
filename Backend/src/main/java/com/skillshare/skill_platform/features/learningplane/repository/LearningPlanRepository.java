package com.linkedin.backend.features.learningplane.repository;

import com.linkedin.backend.features.learningplane.model.LearningPlan;
import com.linkedin.backend.features.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LearningPlanRepository extends JpaRepository<LearningPlan, Long> {
    List<LearningPlan> findByUser(User user);
    List<LearningPlan> findByUserId(Long userId);
} 