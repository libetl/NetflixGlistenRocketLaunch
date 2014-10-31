package com.sage.myrocket.model.rocket;

import java.util.UUID;
import com.amazonaws.services.simpleworkflow.flow.annotations.Execute

import com.netflix.glisten.WorkflowDescriptionTemplate;

class RocketLaunchWorkflowDescriptionTemplate 
    extends WorkflowDescriptionTemplate
    implements RocketLaunchWorkflow {

    @Override
    void launchTheRocket(UUID rocketId) {
        description = "The '${rocketId}' rocket will be launched soon"
        printf "The '${rocketId}' rocket will be launched soon"
    }
}