package com.sage.myrocket.model.doors;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

@Activities(version = "1.0")
@ActivityRegistrationOptions(
    defaultTaskScheduleToStartTimeoutSeconds = -1L,
    defaultTaskStartToCloseTimeoutSeconds = 300L)
public interface CloseTheDoors {

    boolean floorDoorsReadyToClose (Promise<Integer> floorNumber, Promise<Integer> doorNumber)
    
    boolean closeSound ();

    boolean closeDoor (Promise<Integer> floorNumber, Promise<Integer> doorNumber)
}
