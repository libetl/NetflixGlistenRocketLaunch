package com.sage.myrocket.infrastructure.service.printlnrocket;

import groovy.lang.Delegate

import com.amazonaws.services.simpleworkflow.flow.core.Promise
import com.netflix.glisten.ActivityOperations
import com.netflix.glisten.impl.swf.SwfActivityOperations
import com.sage.myrocket.model.doors.CloseMalfunctionException
import com.sage.myrocket.model.doors.CloseTheDoors
import com.sage.myrocket.model.doors.LockMalfunctionException
import com.sage.myrocket.infrastructure.service.util.LuckGenerator

public class CloseTheDoorsImpl implements CloseTheDoors {
    
    @Delegate ActivityOperations activityOperations = new SwfActivityOperations()
    
    @Override
    boolean floorDoorsReadyToClose(int floorNumber,
            int doorNumber) {
        return !LuckGenerator.itHappens (1, 10000)
    }

    @Override
    void closeSound() {
        println "Info : [Buzzer] Tuuuuuuut"
        recordHeartbeat("Info : [Buzzer] Tuuuuuuut")
    }

    @Override
    void closeDoor(int floorNumber,
            int doorNumber)  throws CloseMalfunctionException, LockMalfunctionException {
            println "Info : closing the ${doorNumber} door at the floor ${floorNumber}"
            recordHeartbeat("Info : closing the ${doorNumber} door at the floor ${floorNumber}")
            
            Thread.sleep (1000)
            if (LuckGenerator.itHappens (40, 10000)){
                println "Pblm : ${doorNumber} door at the floor ${floorNumber} : CloseMalfunction"
                recordHeartbeat("Pblm : ${doorNumber} door at the floor ${floorNumber} : CloseMalfunction")
                throw new CloseMalfunctionException ()
            }else if (LuckGenerator.itHappens (40, 10000)){
                println "Pblm : ${doorNumber} door at the floor ${floorNumber} : LockMalfunction"
                recordHeartbeat("Pblm : ${doorNumber} door at the floor ${floorNumber} : LockMalfunction")
                throw new LockMalfunctionException ()
            }else{
                Thread.sleep (1000)
                println "Info : ${doorNumber} door at the floor ${floorNumber} closed and locked"
                recordHeartbeat("Info : ${doorNumber} door at the floor ${floorNumber} closed and locked")
            }
    }

}
