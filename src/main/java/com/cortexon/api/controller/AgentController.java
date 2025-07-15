package com.cortexon.api.controller;

import com.cortexon.api.model.AgentRequest;
import com.cortexon.api.model.AgentResponse;
import com.cortexon.api.service.OrchestratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * REST controller for agent operations
 */
@Slf4j
@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
@Tag(name = "Agent API", description = "API for interacting with agents")
public class AgentController {

    private final OrchestratorService orchestratorService;

    @GetMapping("/chat")
    @Operation(summary = "Process a task", description = "Processes a task and returns a response")
    public CompletableFuture<ResponseEntity<AgentResponse>> chat(@RequestParam String task) {
        log.info("Received chat request with task: {}", task);
        return orchestratorService.processTask(task)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/chat")
    @Operation(summary = "Process a task (POST)", description = "Processes a task and returns a response")
    public CompletableFuture<ResponseEntity<AgentResponse>> chatPost(@Valid @RequestBody AgentRequest request) {
        log.info("Received chat POST request with task: {}", request.getTask());
        return orchestratorService.processTask(request.getTask())
                .thenApply(ResponseEntity::ok);
    }
}