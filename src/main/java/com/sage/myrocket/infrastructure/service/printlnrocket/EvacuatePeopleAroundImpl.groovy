package com.sage.myrocket.infrastructure.service.printlnrocket

import groovy.lang.Delegate;

import com.amazonaws.services.simpleworkflow.flow.core.Promise
import com.netflix.glisten.ActivityOperations;
import com.netflix.glisten.WorkflowOperations;
import com.netflix.glisten.impl.swf.SwfActivityOperations
import com.sage.myrocket.model.engines.EngineFailedToStartException
import com.sage.myrocket.model.engines.FireTheEngines
import com.sage.myrocket.model.people.EvacuatePeopleAround
import com.sage.myrocket.model.people.SomeoneStillInTheLaunchpadException
import com.sage.myrocket.model.people.SpeakerNotWorkingException
import com.netflix.glisten.impl.swf.SwfWorkflowOperations
import com.sage.myrocket.infrastructure.service.util.LuckGenerator

class EvacuatePeopleAroundImpl implements EvacuatePeopleAround {
    
    @Delegate ActivityOperations activityOperations = new SwfActivityOperations()
    
    @Delegate
    WorkflowOperations<EvacuatePeopleAround> evacuateOperations = SwfWorkflowOperations.of(EvacuatePeopleAround)
    
    @Override
    void announceImmediateLaunch() throws SpeakerNotWorkingException {
        if (LuckGenerator.itHappens (50, 1000)){
            println "Pblm : the speaker is not working. Some tests will be lead to understand what happened"
            recordHeartbeat("Pblm : the speaker is not working. Some tests will be lead to understand what happened")
            throw new SpeakerNotWorkingException ()
        }else{
            println "Info : A voice says \"Please leave the building for an immediate launch\""
            recordHeartbeat("Info : A voice says \"Please leave the building for an immediate launch\"")
        }
        true
    }

    @Override
    void floorsCleared() throws SomeoneStillInTheLaunchpadException {
        Thread.sleep (10000)
        if (LuckGenerator.itHappens (10, 1000)){
            println "Pblm : some people are staying in the launch pad"
            recordHeartbeat("Pblm : some people are staying in the launch pad")
            throw new SomeoneStillInTheLaunchpadException ()
        }else{
            println "Info : The launch pad is cleared"
            recordHeartbeat("Info : The launch pad is cleared")
        }
    }


}
