package com.sage.myrocket.infrastructure.service.printlnrocket;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.sage.myrocket.model.doors.CloseTheDoors;

public class CloseTheDoorsImpl implements CloseTheDoors {

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
