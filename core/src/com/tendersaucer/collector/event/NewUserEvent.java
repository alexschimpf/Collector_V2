package com.tendersaucer.collector.event;

/**
 * Created by Alex on 8/1/2016.
 */
public class NewUserEvent extends Event<INewUserEventListener> {

    @Override
    public void notify(INewUserEventListener listener) {
        listener.onNewUser();
    }
}
