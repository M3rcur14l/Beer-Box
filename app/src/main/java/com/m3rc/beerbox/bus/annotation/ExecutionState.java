package com.m3rc.beerbox.bus.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ExecutionState.RUNNING_INITIAL, ExecutionState.RUNNING, ExecutionState.COMPLETED, ExecutionState.FAILED})
@Retention(RetentionPolicy.SOURCE)
public @interface ExecutionState {
    int RUNNING_INITIAL = 1;
    int RUNNING = 2;
    int COMPLETED = 0;
    int FAILED = -1;
}