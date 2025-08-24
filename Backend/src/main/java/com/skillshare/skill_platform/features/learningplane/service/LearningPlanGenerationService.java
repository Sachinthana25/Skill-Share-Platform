package com.linkedin.backend.features.learningplane.service;

import com.linkedin.backend.features.learningplane.dto.LearningPlanGenerationRequest;
import com.linkedin.backend.features.learningplane.model.LearningPlan;
import com.linkedin.backend.features.learningplane.model.Resource;
import com.linkedin.backend.features.learningplane.model.Topic;
import com.linkedin.backend.features.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class LearningPlanGenerationService {
    
    @Autowired
    private LearningPlanService learningPlanService;
    
    private final Random random = new Random();
    
    // Sample topics for different subjects
    private final List<String> mathsTopics = List.of(
        "Introduction to Algebra", "Linear Equations", "Quadratic Equations", 
        "Geometry Basics", "Trigonometry", "Calculus Fundamentals", 
        "Statistics and Probability", "Number Theory", "Matrices"
    );
    
    private final List<String> englishTopics = List.of(
        "Grammar Essentials", "Essay Writing", "Critical Reading", 
        "Literature Analysis", "Creative Writing", "Research Paper Writing", 
        "Public Speaking", "Vocabulary Building", "Rhetoric and Persuasion"
    );
    
    private final List<String> scienceTopics = List.of(
        "Scientific Method", "Physics Fundamentals", "Chemistry Basics", 
        "Biology Essentials", "Earth Science", "Astronomy", 
        "Environmental Science", "Genetics", "Energy and Matter"
    );
    
    // Sample resources for different subjects
    private final List<String[]> mathsResources = List.of(
        new String[]{"Khan Academy Math", "https://www.khanacademy.org/math", "video"},
        new String[]{"MIT OpenCourseWare", "https://ocw.mit.edu/courses/mathematics/", "video"},
        new String[]{"Brilliant - Mathematics", "https://brilliant.org/math/", "link"}
    );
    
    private final List<String[]> englishResources = List.of(
        new String[]{"Purdue Online Writing Lab", "https://owl.purdue.edu/", "document"},
        new String[]{"Grammarly Blog", "https://www.grammarly.com/blog/", "link"},
        new String[]{"TED Talks for English Learners", "https://www.ted.com/", "video"}
    );
    
    private final List<String[]> scienceResources = List.of(
        new String[]{"National Geographic", "https://www.nationalgeographic.com/science/", "link"},
        new String[]{"NASA Science", "https://science.nasa.gov/", "link"},
        new String[]{"SciShow YouTube Channel", "https://www.youtube.com/user/scishow", "video"}
    );
    
    @Transactional
    public LearningPlan generateLearningPlan(LearningPlanGenerationRequest request, User user) {
        LearningPlan plan = new LearningPlan();
        
        // Set basic properties
        plan.setTitle(generateTitle(request.getSubject(), request.getDifficulty()));
        plan.setDescription(request.getDescription() != null && !request.getDescription().isEmpty() 
                           ? request.getDescription() 
                           : generateDescription(request.getSubject(), request.getDifficulty()));
        plan.setSubject(request.getSubject());
        plan.setEstimatedDays(request.getEstimatedDays() != null ? request.getEstimatedDays() : 30);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setCompletionPercentage(0.0);
        plan.setFollowers(0);
        plan.setFollowing(false);
        
        // Create and save plan to get ID
        LearningPlan savedPlan = learningPlanService.createLearningPlan(plan, user);
        
        // Generate topics
        List<Topic> topics = generateTopics(savedPlan, request.getSubject(), request.getDifficulty());
        savedPlan.setTopics(topics);
        
        // Generate resources
        List<Resource> resources = generateResources(savedPlan, request.getSubject());
        savedPlan.setResources(resources);
        
        return savedPlan;
    }
    
    private String generateTitle(String subject, String difficulty) {
        String prefix = "";
        
        switch (difficulty.toLowerCase()) {
            case "beginner":
                prefix = "Introduction to ";
                break;
            case "intermediate":
                prefix = "Mastering ";
                break;
            case "advanced":
                prefix = "Advanced ";
                break;
            default:
                prefix = "Complete Guide to ";
        }
        
        return prefix + capitalizeFirstLetter(subject);
    }
    
    private String generateDescription(String subject, String difficulty) {
        return "A " + difficulty.toLowerCase() + " level learning plan designed to help you master " + 
               subject.toLowerCase() + " concepts through structured topics and curated resources.";
    }
    
    private List<Topic> generateTopics(LearningPlan plan, String subject, String difficulty) {
        List<String> topicPool;
        
        switch (subject.toLowerCase()) {
            case "maths":
                topicPool = mathsTopics;
                break;
            case "english":
                topicPool = englishTopics;
                break;
            case "science":
                topicPool = scienceTopics;
                break;
            default:
                topicPool = new ArrayList<>();
                topicPool.addAll(mathsTopics.subList(0, 3));
                topicPool.addAll(englishTopics.subList(0, 3));
                topicPool.addAll(scienceTopics.subList(0, 3));
        }
        
        // Determine number of topics based on difficulty
        int topicCount;
        switch (difficulty.toLowerCase()) {
            case "beginner":
                topicCount = 5;
                break;
            case "intermediate":
                topicCount = 7;
                break;
            case "advanced":
                topicCount = 9;
                break;
            default:
                topicCount = 6;
        }
        
        // Ensure we don't try to get more topics than available
        topicCount = Math.min(topicCount, topicPool.size());
        
        // Shuffle and pick a subset
        List<String> shuffledTopics = new ArrayList<>(topicPool);
        java.util.Collections.shuffle(shuffledTopics, random);
        List<String> selectedTopics = shuffledTopics.subList(0, topicCount);
        
        // Create Topic entities
        List<Topic> topics = new ArrayList<>();
        for (String topicTitle : selectedTopics) {
            Topic topic = new Topic();
            topic.setTitle(topicTitle);
            topic.setCompleted(false);
            topic.setLearningPlan(plan);
            topics.add(topic);
        }
        
        return topics;
    }
    
    private List<Resource> generateResources(LearningPlan plan, String subject) {
        List<String[]> resourcePool;
        
        switch (subject.toLowerCase()) {
            case "maths":
                resourcePool = mathsResources;
                break;
            case "english":
                resourcePool = englishResources;
                break;
            case "science":
                resourcePool = scienceResources;
                break;
            default:
                resourcePool = new ArrayList<>();
                resourcePool.addAll(mathsResources);
                resourcePool.addAll(englishResources.subList(0, 1));
                resourcePool.addAll(scienceResources.subList(0, 1));
        }
        
        // Create Resource entities
        List<Resource> resources = new ArrayList<>();
        for (String[] resourceData : resourcePool) {
            Resource resource = new Resource();
            resource.setTitle(resourceData[0]);
            resource.setUrl(resourceData[1]);
            resource.setType(resourceData[2]);
            resource.setLearningPlan(plan);
            resources.add(resource);
        }
        
        return resources;
    }
    
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
} 