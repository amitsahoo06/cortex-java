package com.cortexon.api.service.impl;

import com.cortexon.api.service.PlannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of the PlannerService interface
 */
@Slf4j
@Service
public class PlannerServiceImpl implements PlannerService {

    @Value("${ai.model.anthropic.model-name}")
    private String modelName;

    private static final String TODO_FILE_PATH = "planner/todo.md";
    private static final Pattern TASK_PATTERN = Pattern.compile("- \\[ \\] (.+)");

    @Override
    public String createPlan(String task) {
        log.info("Creating plan for task: {}", task);
        
        // In a real implementation, this would call an AI model
        // For now, we'll create a simple plan
        StringBuilder planBuilder = new StringBuilder();
        planBuilder.append("# Task Plan\n\n");
        planBuilder.append("## Task: ").append(task).append("\n\n");
        planBuilder.append("## Steps:\n\n");
        
        // Add some sample steps
        planBuilder.append("- [ ] Analyze the task requirements\n");
        planBuilder.append("- [ ] Research necessary information\n");
        planBuilder.append("- [ ] Implement the solution\n");
        planBuilder.append("- [ ] Test the implementation\n");
        planBuilder.append("- [ ] Deliver the final result\n");
        
        String plan = planBuilder.toString();
        
        // Save the plan to a file
        savePlan(plan);
        
        return plan;
    }

    @Override
    public String updatePlan(String completedTask) {
        log.info("Updating plan with completed task: {}", completedTask);
        
        try {
            // Read the current plan
            String currentPlan = readPlan();
            
            // If no plan exists, create a new one
            if (currentPlan == null || currentPlan.isEmpty()) {
                return createPlan("Task based on: " + completedTask);
            }
            
            // Update the plan by marking the completed task
            List<String> lines = new ArrayList<>(Files.readAllLines(getPlanPath()));
            boolean taskFound = false;
            
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.contains(completedTask.replace(" (coder_agent)", "")
                        .replace(" (web_surfer_agent)", ""))) {
                    // Mark the task as completed
                    lines.set(i, line.replace("- [ ]", "- [x]"));
                    taskFound = true;
                    break;
                }
            }
            
            // If the task wasn't found, add it as a completed task
            if (!taskFound) {
                lines.add("- [x] " + completedTask);
            }
            
            // Write the updated plan back to the file
            String updatedPlan = String.join("\n", lines);
            savePlan(updatedPlan);
            
            return updatedPlan;
        } catch (IOException e) {
            log.error("Error updating plan", e);
            return "Error updating plan: " + e.getMessage();
        }
    }
    
    private void savePlan(String plan) {
        try {
            Path planPath = getPlanPath();
            
            // Create directories if they don't exist
            Files.createDirectories(planPath.getParent());
            
            // Write the plan to the file
            Files.writeString(planPath, plan);
            log.info("Plan saved to {}", planPath);
        } catch (IOException e) {
            log.error("Error saving plan", e);
        }
    }
    
    private String readPlan() {
        try {
            Path planPath = getPlanPath();
            
            if (Files.exists(planPath)) {
                return Files.readString(planPath);
            }
            
            return null;
        } catch (IOException e) {
            log.error("Error reading plan", e);
            return null;
        }
    }
    
    private Path getPlanPath() {
        return Paths.get(System.getProperty("user.dir"), TODO_FILE_PATH);
    }
}