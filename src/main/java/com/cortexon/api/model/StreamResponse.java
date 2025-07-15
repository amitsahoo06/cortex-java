package com.cortexon.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a streaming response from an agent
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StreamResponse {
    
    private String agentName;
    private String instructions;
    
    @Builder.Default
    private List<String> steps = new ArrayList<>();
    
    private String output;
    private int statusCode;
    private String liveUrl;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Adds a step to the steps list
     * 
     * @param step The step to add
     * @return This StreamResponse instance for method chaining
     */
    public StreamResponse addStep(String step) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        this.steps.add(step);
        return this;
    }
}