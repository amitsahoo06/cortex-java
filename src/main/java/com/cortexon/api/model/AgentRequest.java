package com.cortexon.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request to an agent
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequest {
    
    @NotBlank(message = "Task cannot be empty")
    private String task;
}