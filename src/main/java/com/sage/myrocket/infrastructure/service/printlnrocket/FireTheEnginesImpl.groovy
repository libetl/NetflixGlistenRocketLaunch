package com.sage.myrocket.infrastructure.service.printlnrocket;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.netflix.glisten.ActivityOperations
import com.netflix.glisten.impl.swf.SwfActivityOperations
import com.sage.myrocket.model.engines.EngineFailedToStartException;
import com.sage.myrocket.model.engines.FireTheEngines;
import com.sage.myrocket.model.engines.ThrottleException

import com.sage.myrocket.infrastructure.service.util.LuckGenerator

public class FireTheEnginesImpl implements FireTheEngines {

    @Delegate ActivityOperations activityOperations = new SwfActivityOperations()

    @Override
    public Promise<Void> preheating (int seconds) {
        for (i in seconds..1){
            recordHeartbeat("Info : preHeating : ${i} seconds remaining")
            if (i % 10 == 0){
                println "Info : preHeating : ${i} seconds remaining"
                recordHeartbeat("Info : preHeating : ${i} seconds remaining")
            }
            Thread.sleep(1);
        }

    }

    @Override
    public void startEngine(int engineNumber)
    throws EngineFailedToStartException {
        if (LuckGenerator.itHappens (50, 1000)){
            println "Pblm : the engine ${engineNumber} did not start"
            recordHeartbeat("Pblm : the engine ${engineNumber} did not start")
            throw new EngineFailedToStartException ()
        }else{
            println "Info : Engine ${engineNumber} started"
            recordHeartbeat("Info : Engine ${engineNumber} started")
        }
    }

    @Override
    public void throttle() throws ThrottleException {
        if (LuckGenerator.itHappens (8, 1000)){
            recordHeartbeat("Pblm : Not enough engine power to take off")
            throw new ThrottleException ()
        }else{
            println "Info : Launch Successful"
            recordHeartbeat("Info : Launch Successful")
        }
    }


}
