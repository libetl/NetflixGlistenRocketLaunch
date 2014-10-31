package com.sage.myrocket.infrastructure.service.printlnrocket

import groovy.lang.Delegate;

import com.amazonaws.services.simpleworkflow.flow.core.Promise
import com.netflix.glisten.ActivityOperations;
import com.netflix.glisten.WorkflowOperations;
import com.netflix.glisten.impl.swf.SwfActivityOperations
import com.sage.myrocket.model.engines.FailedToStartException
import com.sage.myrocket.model.engines.FireTheEngines
import com.sage.myrocket.model.people.EvacuatePeopleAround
import com.netflix.glisten.impl.swf.SwfWorkflowOperations

class EvacuatePeopleAroundImpl implements EvacuatePeopleAround {
    
    @Delegate ActivityOperations activityOperations = new SwfActivityOperations()
    
    @Delegate
    WorkflowOperations<EvacuatePeopleAround> evacuateOperations = SwfWorkflowOperations.of(EvacuatePeopleAround)
    
    @Override
    boolean announceImmediateLaunch() {
        println "Please leave the building for immediate launch" 
        status "Please leave the building for immediate launch" 
        true
    }

    @Override
    Promise<Boolean> floorsCleared() {
        Thread.sleep (10000)
        if (Math.random() % 10 > 8){
            println "Pblm : some people are staying in the launch pad"
            status  "Pblm : some people are staying in the launch pad"
            return Promise.asPromise(false)
        }
        println "Info : The launch pad is cleared"
        status  "Info : The launch pad is cleared"
        return Promise.asPromise(true)
    }


}
