package com.linkedin.backend.features.learningplane.dto;

import com.linkedin.backend.features.learningplane.model.LearningPlan;
import com.linkedin.backend.features.learningplane.model.Resource;
import com.linkedin.backend.features.learningplane.model.Topic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for LearningPlan to avoid serializing Hibernate entities directly
 */
public class LearningPlanDTO {
    private Long id;
    private String title;
    private String description;
    private String subject;
    private double completionPercentage;
    private int estimatedDays;
    private int followers;
    private LocalDateTime createdAt;
    private boolean following;
    private UserSummaryDTO user;
    private List<TopicDTO> topics;
    private List<ResourceDTO> resources;
    
    // Default constructor for Jackson
    public LearningPlanDTO() {
    }

    /**
     * Constructor to create a DTO from a LearningPlan entity
     */
    public LearningPlanDTO(LearningPlan plan) {
        this.id = plan.getId();
        this.title = plan.getTitle();
        this.description = plan.getDescription();
        this.subject = plan.getSubject();
        this.completionPercentage = plan.getCompletionPercentage();
        this.estimatedDays = plan.getEstimatedDays();
        this.followers = plan.getFollowers();
        this.createdAt = plan.getCreatedAt();
        this.following = plan.isFollowing();
        
        if (plan.getUser() != null) {
            this.user = new UserSummaryDTO(plan.getUser());
        }
        
        if (plan.getTopics() != null) {
            this.topics = plan.getTopics().stream()
                .map(TopicDTO::new)
                .collect(Collectors.toList());
        }
        
        if (plan.getResources() != null) {
            this.resources = plan.getResources().stream()
                .map(ResourceDTO::new)
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Convert a list of LearningPlan entities to DTOs
     */
    public static List<LearningPlanDTO> fromEntities(List<LearningPlan> plans) {
        return plans.stream()
            .map(LearningPlanDTO::new)
            .collect(Collectors.toList());
    }
    
    // Inner DTOs for related entities
    
    public static class TopicDTO {
        private Long id;
        private String title;
        private boolean completed;
        
        // Default constructor for Jackson
        public TopicDTO() {
        }
        
        public TopicDTO(Topic topic) {
            // Access fields directly if getters aren't available
            try {
                java.lang.reflect.Field idField = Topic.class.getDeclaredField("id");
                java.lang.reflect.Field titleField = Topic.class.getDeclaredField("title");
                java.lang.reflect.Field completedField = Topic.class.getDeclaredField("completed");
                
                idField.setAccessible(true);
                titleField.setAccessible(true);
                completedField.setAccessible(true);
                
                this.id = (Long) idField.get(topic);
                this.title = (String) titleField.get(topic);
                this.completed = (boolean) completedField.get(topic);
            } catch (Exception e) {
                // Fallback with null values if reflection fails
                this.id = null;
                this.title = "Unknown Topic";
                this.completed = false;
            }
        }
        
        // Getters
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public boolean isCompleted() { return completed; }
    }
    
    public static class ResourceDTO {
        private Long id;
        private String title;
        private String url;
        private String type;
        
        // Default constructor for Jackson
        public ResourceDTO() {
        }
        
        public ResourceDTO(Resource resource) {
            // Access fields directly if getters aren't available
            try {
                java.lang.reflect.Field idField = Resource.class.getDeclaredField("id");
                java.lang.reflect.Field titleField = Resource.class.getDeclaredField("title");
                java.lang.reflect.Field urlField = Resource.class.getDeclaredField("url");
                java.lang.reflect.Field typeField = Resource.class.getDeclaredField("type");
                
                idField.setAccessible(true);
                titleField.setAccessible(true);
                urlField.setAccessible(true);
                typeField.setAccessible(true);
                
                this.id = (Long) idField.get(resource);
                this.title = (String) titleField.get(resource);
                this.url = (String) urlField.get(resource);
                this.type = (String) typeField.get(resource);
            } catch (Exception e) {
                // Fallback with null values if reflection fails
                this.id = null;
                this.title = "Unknown Resource";
                this.url = "#";
                this.type = "link";
            }
        }
        
        // Getters
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getUrl() { return url; }
        public String getType() { return type; }
    }
    
    public static class UserSummaryDTO {
        private Long id;
        private String name;
        private String username;
        private String profilePicture;
        
        // Default constructor for Jackson
        public UserSummaryDTO() {
        }
        
        public UserSummaryDTO(com.linkedin.backend.features.authentication.model.User user) {
            this.id = user.getId();
            this.name = (user.getFirstName() != null && user.getLastName() != null) 
                ? user.getFirstName() + " " + user.getLastName() 
                : user.getEmail();
            // Use email as username if not available
            this.username = user.getEmail().split("@")[0];
            this.profilePicture = user.getProfilePicture();
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getUsername() { return username; }
        public String getProfilePicture() { return profilePicture; }
    }
    
    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSubject() { return subject; }
    public double getCompletionPercentage() { return completionPercentage; }
    public int getEstimatedDays() { return estimatedDays; }
    public int getFollowers() { return followers; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isFollowing() { return following; }
    public UserSummaryDTO getUser() { return user; }
    public List<TopicDTO> getTopics() { return topics; }
    public List<ResourceDTO> getResources() { return resources; }
} 