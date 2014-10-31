package com.sage.myrocket.infrastructure.service.printlnrocket

import com.amazonaws.services.simpleworkflow.flow.core.Promise
import com.sage.myrocket.model.engines.FailedToStartException
import com.sage.myrocket.model.engines.FireTheEngines
import com.sage.myrocket.model.people.EvacuatePeopleAround

class EvacuatePeopleAroundImpl implements EvacuatePeopleAround {

    @Override
    boolean announceImmediateLaunch() {
        printf "Please leave the building for immediate launch" 
        true
    }

    @Override
    Promise<Boolean> floorsCleared() {
        timer(10, 'people leaves the building')
        if (Math.random() * 10 > 9){
            printf "Warn : some people are staying in the launch pad"
            Promise.asPromise(false);
        }
        Promise.asPromise(true);
    }


}
