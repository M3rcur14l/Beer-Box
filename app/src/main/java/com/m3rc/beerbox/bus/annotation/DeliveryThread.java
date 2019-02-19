package com.m3rc.beerbox.bus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({DeliveryThread.MAIN_THREAD, DeliveryThread.IO_THREAD, DeliveryThread.NEW_THREAD})
@Retention(RetentionPolicy.SOURCE)
public @interface DeliveryThread {
    int MAIN_THREAD = 0;
    int IO_THREAD = 1;
    int NEW_THREAD = 2;
}