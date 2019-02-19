package com.m3rc.beerbox.bus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({ExecutionState.RUNNING, ExecutionState.COMPLETED, ExecutionState.FAILED})
@Retention(RetentionPolicy.SOURCE)
public @interface ExecutionState {
    int RUNNING = 1;
    int COMPLETED = 0;
    int FAILED = -1;
}