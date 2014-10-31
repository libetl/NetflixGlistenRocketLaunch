package com.sage.myrocket.model.people;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;

@Activities(version = "1.0")
@ActivityRegistrationOptions(
    defaultTaskScheduleToStartTimeoutSeconds = -1L,
    defaultTaskStartToCloseTimeoutSeconds = 300L)
interface EvacuatePeopleAround {

    boolean announceImmediateLaunch ()
    
    void floorsCleared()
}
