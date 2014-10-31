package com.sage.myrocket.infrastructure.service.printlnrocket;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.netflix.glisten.ActivityOperations
import com.netflix.glisten.impl.swf.SwfActivityOperations
import com.sage.myrocket.model.engines.FailedToStartException;
import com.sage.myrocket.model.engines.FireTheEngines;

public class FireTheEnginesImpl implements FireTheEngines {
    
    @Delegate ActivityOperations activityOperations = new SwfActivityOperations()
    
    @Override
    public int preheating(Promise<Integer> seconds) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int preHeatingRemainingSeconds() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean startEngine(Promise<Integer> engineNumber) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void throttle() throws FailedToStartException {
        // TODO Auto-generated method stub

    }

}
