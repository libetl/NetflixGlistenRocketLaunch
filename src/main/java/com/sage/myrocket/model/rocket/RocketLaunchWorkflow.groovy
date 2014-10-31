package com.sage.myrocket.model.rocket;

import java.util.UUID;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.GetState

@Workflow
@WorkflowRegistrationOptions(
        description="Sandbox Test Workflow", 
        defaultExecutionStartToCloseTimeoutSeconds = 60L)
interface RocketLaunchWorkflow {

    /**
     * @param rocketId
     */
    @Execute (version = "1.0")
    void launchTheRocket (UUID rocketId)
    
    @GetState
    List<String> getLogHistory()
}
