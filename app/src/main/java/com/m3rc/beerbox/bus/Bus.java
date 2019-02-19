package com.m3rc.beerbox.bus;

import com.m3rc.beerbox.bus.annotation.DeliveryThread;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by  Antonello Fodde on 10/04/2017.
 * antonello.fodde@accenture.com
 */
public class Bus {

    private static Bus mBus;

    private final Map<Class<?>, Subject<Object>> mEventBusSubjectMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, Subject<Object>> mStateBusSubjectMap = new ConcurrentHashMap<>();

    private Bus() {
    }

    public static synchronized Bus get() {
        if (mBus == null)
            mBus = new Bus();
        return mBus;
    }

    public <T> Disposable subscribeToEvent(final Class<T> eventClass, Consumer<T> onNext) {
        return subscribeToEvent(eventClass, onNext, DeliveryThread.MAIN_THREAD);
    }

    public <T> Disposable subscribeToState(final Class<T> stateClass, Consumer<T> onNext) {
        return subscribeToState(stateClass, onNext, DeliveryThread.MAIN_THREAD);
    }

    public <T> Disposable subscribeToEvent(final Class<T> eventClass, Consumer<T> onNext,
                                           @DeliveryThread int deliveryThread) {
        eventSubjectCheck(eventClass);
        return mEventBusSubjectMap.get(eventClass)
                .cast(eventClass)
                .observeOn(getScheduler(deliveryThread))
                .subscribe(onNext);
    }

    public <T> Disposable subscribeToState(final Class<T> stateClass, Consumer<T> onNext,
                                           @DeliveryThread int deliveryThread) {
        stateSubjectCheck(stateClass);
        return mStateBusSubjectMap.get(stateClass)
                .cast(stateClass)
                .observeOn(getScheduler(deliveryThread))
                .subscribe(onNext);
    }

    public <T> boolean hasSubscriberForEvent(final Class<T> eventClass) {
        eventSubjectCheck(eventClass);
        return mEventBusSubjectMap.get(eventClass).hasObservers();
    }

    @SuppressWarnings("unused")
    public <T> boolean hasSubscriberForState(final Class<T> stateClass) {
        stateSubjectCheck(stateClass);
        return mStateBusSubjectMap.get(stateClass).hasObservers();
    }

    public void postEvent(Object event) {
        eventSubjectCheck(event.getClass());
        mEventBusSubjectMap.get(event.getClass()).onNext(event);
    }

    public Disposable postDelayedEvent(Object event, long delay, TimeUnit unit) {
        return Observable.timer(delay, unit)
                .subscribe(l -> postEvent(event));
    }

    public void postState(Object state) {
        stateSubjectCheck(state.getClass());
        mStateBusSubjectMap.get(state.getClass()).onNext(state);
    }

    public Disposable postDelayedState(Object state, long delay, TimeUnit unit) {
        return Observable.timer(delay, unit)
                .subscribe(l -> postState(state));
    }

    private <T> void eventSubjectCheck(Class<T> eventClass) {
        if (!mEventBusSubjectMap.containsKey(eventClass))
            mEventBusSubjectMap.put(eventClass, PublishSubject.create().toSerialized());
    }

    private <T> void stateSubjectCheck(Class<T> stateClass) {
        if (!mStateBusSubjectMap.containsKey(stateClass))
            mStateBusSubjectMap.put(stateClass, BehaviorSubject.create().toSerialized());
    }

    private Scheduler getScheduler(@DeliveryThread int deliveryThread) {
        switch (deliveryThread) {
            case DeliveryThread.MAIN_THREAD:
                return AndroidSchedulers.mainThread();
            case DeliveryThread.IO_THREAD:
                return Schedulers.io();
            case DeliveryThread.NEW_THREAD:
                return Schedulers.newThread();
            default:
                return AndroidSchedulers.mainThread();
        }
    }
}
