package com.sage.myrocket.model.engines;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

@Activities(version = "1.0")
@ActivityRegistrationOptions(
    defaultTaskScheduleToStartTimeoutSeconds = -1L,
    defaultTaskStartToCloseTimeoutSeconds = 300L)
interface FireTheEngines {
    
    @Asynchronous
    Promise<Void> preheating (int seconds)
    
    void startEngine (int engineNumber) throws EngineFailedToStartException
    
    void throttle () throws ThrottleException
}
