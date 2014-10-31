package com.sage.myrocket.model.countdown;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

interface CountdownEndCallback {

    void endOfCountdown ()
}
