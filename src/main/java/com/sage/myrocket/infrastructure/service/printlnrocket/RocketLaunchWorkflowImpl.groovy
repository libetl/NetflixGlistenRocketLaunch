package com.sage.myrocket.infrastructure.service.printlnrocket;

import java.util.UUID;

import com.amazonaws.services.simpleworkflow.flow.core.Promise
import com.netflix.glisten.WorkflowOperations
import com.sage.myrocket.model.countdown.CountdownEndCallback;
import com.sage.myrocket.model.doors.CloseTheDoors
import com.sage.myrocket.model.engines.FireTheEngines
import com.sage.myrocket.model.people.EvacuatePeopleAround
import com.sage.myrocket.model.rocket.RocketLaunchWorkflow

class RocketLaunchWorkflowImpl implements RocketLaunchWorkflow, CountdownEndCallback {

    @Delegate
    WorkflowOperations<FireTheEngines> enginesOperations = SwfWorkflowOperations.of(FireTheEngines)
    @Delegate
    WorkflowOperations<EvacuatePeopleAround> evacuateOperations = SwfWorkflowOperations.of(EvacuatePeopleAround)
    @Delegate
    WorkflowOperations<CloseTheDoors> closeOperations = SwfWorkflowOperations.of(CloseTheDoors)

    @Override
    void launchTheRocket(UUID rocketId) {
        def rocketFloors = 4
        def doorsPerFloor = 3
        def preheatingTime = promiseFor (360)
        
        getEnginesOperations().activities.preheating(preheatingTime)
        getEvacuateOperations().activities.announceImmediateLaunch()
        waitFor(getEvacuateOperations().activities.floorsCleared())
        for (floor in 1..rocketFloors){
            for (door in 1..doorsPerFloor){
                doTry {
                    getCloseOperations().activities.floorDoorsReadyToClose(promiseFor(floor), promiseFor(door))
                    getCloseOperations().activities.closeDoor(promiseFor(floor), promiseFor(door))
                }.withCatch {
                    Throwable t ->
                        status "Floor ${floor}, Door ${door} : door is locked. Human intervention required"
                    
                }
            }
        }
        Promise.Void()
    }

    @Override
    void endOfCountdown() {
        
    }

}
