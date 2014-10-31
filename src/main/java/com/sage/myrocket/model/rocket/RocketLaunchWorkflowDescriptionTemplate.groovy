package com.sage.myrocket.model.rocket;

import java.util.UUID;
import com.amazonaws.services.simpleworkflow.flow.annotations.Execute

import com.netflix.glisten.WorkflowDescriptionTemplate;

class RocketLaunchWorkflowDescriptionTemplate 
    extends WorkflowDescriptionTemplate
    implements RocketLaunchWorkflow {

    @Override
    void launchTheRocket(UUID rocketId) {
        setDescription("${rocketId} rocket will be launched soon")
    }
}