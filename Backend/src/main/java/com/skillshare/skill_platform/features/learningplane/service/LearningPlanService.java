package com.linkedin.backend.features.learningplane.service;

import com.linkedin.backend.features.learningplane.model.LearningPlan;
import com.linkedin.backend.features.learningplane.model.Topic;
import com.linkedin.backend.features.learningplane.repository.LearningPlanRepository;
import com.linkedin.backend.features.learningplane.repository.TopicRepository;
import com.linkedin.backend.features.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
public class LearningPlanService {
    @Autowired
    private LearningPlanRepository learningPlanRepository;
    
    @Autowired
    private TopicRepository topicRepository;

    @Transactional(readOnly = true)
    public List<LearningPlan> getAllLearningPlans(User user, String userId) {
        if (userId != null && !userId.isEmpty()) {
            try {
                Long userIdLong = Long.parseLong(userId);
                return learningPlanRepository.findByUserId(userIdLong);
            } catch (NumberFormatException e) {
                return List.of(); // Return empty list if userId is not a valid Long
            }
        }
        return learningPlanRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<LearningPlan> getLearningPlanById(Long id, User user) {
        return learningPlanRepository.findById(id);
    }

    @Transactional
    public LearningPlan createLearningPlan(LearningPlan plan, User user) {
        plan.setUser(user);
        plan.setFollowers(0);
        plan.setFollowing(false);
        return learningPlanRepository.save(plan);
    }

    @Transactional
    public void deleteLearningPlan(Long id, User user) {
        learningPlanRepository.findById(id).ifPresent(plan -> {
            if (plan.getUser().getId().equals(user.getId())) {
                learningPlanRepository.deleteById(id);
            }
        });
    }

    @Transactional
    public LearningPlan updateLearningPlan(Long id, LearningPlan updatedPlan, User user) {
        return learningPlanRepository.findById(id)
            .filter(plan -> plan.getUser().getId().equals(user.getId()))
            .map(plan -> {
                updatedPlan.setId(id);
                updatedPlan.setUser(user);
                updatedPlan.setFollowers(plan.getFollowers());
                updatedPlan.setFollowing(plan.isFollowing());
                return learningPlanRepository.save(updatedPlan);
            })
            .orElseThrow();
    }

    @Transactional
    public LearningPlan followPlan(Long id, User user) {
        return learningPlanRepository.findById(id)
            .map(plan -> {
                plan.setFollowers(plan.getFollowers() + 1);
                plan.setFollowing(true);
                return learningPlanRepository.save(plan);
            })
            .orElseThrow();
    }

    @Transactional
    public LearningPlan unfollowPlan(Long id, User user) {
        return learningPlanRepository.findById(id)
            .map(plan -> {
                plan.setFollowers(plan.getFollowers() - 1);
                plan.setFollowing(false);
                return learningPlanRepository.save(plan);
            })
            .orElseThrow();
    }
    
    @Transactional
    public LearningPlan toggleTopicCompletion(Long planId, Long topicId, User user) {
        LearningPlan plan = learningPlanRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Learning plan not found"));
        
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException("Topic not found"));
        
        // Verify the topic belongs to the plan
        if (!topic.getLearningPlan().getId().equals(planId)) {
            throw new IllegalArgumentException("Topic does not belong to the specified learning plan");
        }
        
        // Toggle completion status
        topic.setCompleted(!topic.isCompleted());
        topicRepository.save(topic);
        
        // Update completion percentage
        updatePlanCompletionPercentage(plan);
        
        return learningPlanRepository.save(plan);
    }
    
    private void updatePlanCompletionPercentage(LearningPlan plan) {
        List<Topic> topics = plan.getTopics();
        
        if (topics == null || topics.isEmpty()) {
            plan.setCompletionPercentage(0);
            return;
        }
        
        long completedCount = topics.stream()
                .filter(Topic::isCompleted)
                .count();
        
        double percentage = (double) completedCount / topics.size() * 100;
        plan.setCompletionPercentage(percentage);
    }
} 