package com.linkedin.backend.features.learningplane.util;

import com.linkedin.backend.features.learningplane.model.LearningPlan;

/**
 * Utility class for initializing lazy-loaded Hibernate entities
 * to prevent serialization issues.
 */
public class EntityInitializer {
    
    /**
     * Initialize all lazy-loaded collections and relationships in a LearningPlan
     * 
     * @param plan The LearningPlan to initialize
     * @return The same LearningPlan after initialization
     */
    public static LearningPlan initializeLearningPlan(LearningPlan plan) {
        if (plan == null) {
            return null;
        }
        
        // Initialize lazy collections
        if (plan.getTopics() != null) {
            plan.getTopics().size(); // Force initialization
        }
        if (plan.getResources() != null) {
            plan.getResources().size(); // Force initialization
        }
        if (plan.getUser() != null) {
            // Access a non-sensitive property to initialize the user
            plan.getUser().getId();
        }
        
        return plan;
    }
} 