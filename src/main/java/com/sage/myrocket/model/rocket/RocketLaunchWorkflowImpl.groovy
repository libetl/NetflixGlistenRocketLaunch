package com.sage.myrocket.model.rocket;

import java.util.UUID;

import com.amazonaws.services.simpleworkflow.flow.core.Promise
import com.netflix.glisten.WorkflowOperations
import com.sage.myrocket.model.doors.CloseTheDoors
import com.sage.myrocket.model.engines.FireTheEngines
import com.sage.myrocket.model.people.EvacuatePeopleAround
import com.sage.myrocket.model.rocket.RocketLaunchWorkflow
import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.netflix.glisten.impl.swf.SwfWorkflowOperations

public class RocketLaunchWorkflowImpl implements RocketLaunchWorkflow {

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
        def preheatingTime = 360
        def engineNumber = 5

        def preheatedEngines = getEnginesOperations().activities.preheating(preheatingTime)
        getEvacuateOperations().activities.announceImmediateLaunch()
        waitFor(getEvacuateOperations().activities.floorsCleared()){
            getCloseOperations().activities.closeSound()
            for (floor in 1..rocketFloors){
                for (door in 1..doorsPerFloor){
                    if (getCloseOperations().activities.floorDoorsReadyToClose(floor, door)){
                        getCloseOperations().activities.closeDoor(floor, door)
                    }else{
                        throw new RocketLaunchException ()
                    }
                }
            }
            
            waitFor(preheatedEngines){
                for (engine in 1..engineNumber){
                    getEnginesOperations().activities.startEngine(engine)
                }
            
                getEnginesOperations().activities.throttle()
                Promise.Void()
            }
        }
        
    }

}
