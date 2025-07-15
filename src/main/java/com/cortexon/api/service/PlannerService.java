package com.cortexon.api.service;

/**
 * Service interface for planning tasks
 */
public interface PlannerService {

    /**
     * Creates a plan for a task
     *
     * @param task The task to plan
     * @return The plan as a string
     */
    String createPlan(String task);

    /**
     * Updates a plan with a completed task
     *
     * @param completedTask The completed task
     * @return The updated plan
     */
    String updatePlan(String completedTask);
}