package com.sage.myrocket.infrastructure.service.printlnrocket;

import groovy.lang.Delegate;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.netflix.glisten.ActivityOperations;
import com.netflix.glisten.impl.swf.SwfActivityOperations
import com.sage.myrocket.model.doors.CloseTheDoors;

public class CloseTheDoorsImpl implements CloseTheDoors {
    
    @Delegate ActivityOperations activityOperations = new SwfActivityOperations()
    
    @Override
    public boolean floorDoorsReadyToClose(Promise<Integer> floorNumber,
            Promise<Integer> doorNumber) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean closeSound() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean closeDoor(Promise<Integer> floorNumber,
            Promise<Integer> doorNumber) {
        // TODO Auto-generated method stub
        return false;
    }

}
