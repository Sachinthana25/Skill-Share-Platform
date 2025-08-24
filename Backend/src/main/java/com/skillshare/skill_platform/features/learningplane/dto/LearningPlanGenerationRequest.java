package com.linkedin.backend.features.learningplane.dto;

/**
 * Data Transfer Object for requesting AI-generated learning plans
 */
public class LearningPlanGenerationRequest {
    private String subject;
    private String difficulty;
    private Integer estimatedDays;
    private String description;

    // Default constructor for Jackson
    public LearningPlanGenerationRequest() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 