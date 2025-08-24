package com.linkedin.backend.features.learningplane.controller;

import com.linkedin.backend.features.learningplane.dto.LearningPlanDTO;
import com.linkedin.backend.features.learningplane.model.LearningPlan;
import com.linkedin.backend.features.learningplane.service.LearningPlanService;
import com.linkedin.backend.features.learningplane.util.EntityInitializer;
import com.linkedin.backend.features.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/learning-plans")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LearningPlanController {
    @Autowired
    private LearningPlanService learningPlanService;

    @GetMapping
    public ResponseEntity<List<LearningPlanDTO>> getAllLearningPlans(
            @RequestAttribute("authenticatedUser") User user,
            @RequestParam(required = false) String userId) {
        try {
            List<LearningPlan> plans = learningPlanService.getAllLearningPlans(user, userId);
            // Convert to DTOs instead of initializing entities
            List<LearningPlanDTO> dtoList = LearningPlanDTO.fromEntities(plans);
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPlanDTO> getLearningPlanById(
            @PathVariable Long id,
            @RequestAttribute("authenticatedUser") User user) {
        try {
            return learningPlanService.getLearningPlanById(id, user)
                    .map(plan -> {
                        // Convert to DTO
                        LearningPlanDTO dto = new LearningPlanDTO(plan);
                        return ResponseEntity.ok(dto);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<LearningPlanDTO> createLearningPlan(
            @RequestBody LearningPlan plan,
            @RequestAttribute("authenticatedUser") User user) {
        try {
            LearningPlan createdPlan = learningPlanService.createLearningPlan(plan, user);
            // Convert to DTO
            LearningPlanDTO dto = new LearningPlanDTO(createdPlan);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningPlanDTO> updateLearningPlan(
            @PathVariable Long id,
            @RequestBody LearningPlan plan,
            @RequestAttribute("authenticatedUser") User user) {
        try {
            LearningPlan updatedPlan = learningPlanService.updateLearningPlan(id, plan, user);
            // Convert to DTO
            LearningPlanDTO dto = new LearningPlanDTO(updatedPlan);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningPlan(
            @PathVariable Long id,
            @RequestAttribute("authenticatedUser") User user) {
        try {
            learningPlanService.deleteLearningPlan(id, user);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<LearningPlanDTO> followPlan(
            @PathVariable Long id,
            @RequestAttribute("authenticatedUser") User user) {
        try {
            LearningPlan plan = learningPlanService.followPlan(id, user);
            // Convert to DTO
            LearningPlanDTO dto = new LearningPlanDTO(plan);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<LearningPlanDTO> unfollowPlan(
            @PathVariable Long id,
            @RequestAttribute("authenticatedUser") User user) {
        try {
            LearningPlan plan = learningPlanService.unfollowPlan(id, user);
            // Convert to DTO
            LearningPlanDTO dto = new LearningPlanDTO(plan);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/{planId}/topics/{topicId}/toggle-completion")
    public ResponseEntity<LearningPlanDTO> toggleTopicCompletion(
            @PathVariable Long planId,
            @PathVariable Long topicId,
            @RequestAttribute("authenticatedUser") User user) {
        try {
            LearningPlan plan = learningPlanService.toggleTopicCompletion(planId, topicId, user);
            // Convert to DTO
            LearningPlanDTO dto = new LearningPlanDTO(plan);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
} 