package com.linkedin.backend.features.learningplane.controller;

import com.linkedin.backend.features.learningplane.dto.LearningPlanDTO;
import com.linkedin.backend.features.learningplane.dto.LearningPlanGenerationRequest;
import com.linkedin.backend.features.learningplane.model.LearningPlan;
import com.linkedin.backend.features.learningplane.service.LearningPlanGenerationService;
import com.linkedin.backend.features.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learning-plans/generate")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LearningPlanGenerationController {
    
    @Autowired
    private LearningPlanGenerationService learningPlanGenerationService;
    
    @PostMapping
    public ResponseEntity<LearningPlanDTO> generateLearningPlan(
            @RequestBody LearningPlanGenerationRequest request,
            @RequestAttribute("authenticatedUser") User user) {
        try {
            LearningPlan generatedPlan = learningPlanGenerationService.generateLearningPlan(request, user);
            // Convert to DTO
            LearningPlanDTO dto = new LearningPlanDTO(generatedPlan);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
} 