package com.sage.myrocket.model.engines;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

@Activities(version = "1.0")
@ActivityRegistrationOptions(
    defaultTaskScheduleToStartTimeoutSeconds = -1L,
    defaultTaskStartToCloseTimeoutSeconds = 300L)
public interface FireTheEngines {

    int preheating (Promise<Integer> seconds)
    
    int preHeatingRemainingSeconds ();
    
    boolean startEngine (Promise<Integer> engineNumber)
    
    void start () throws FailedToStartException
}
