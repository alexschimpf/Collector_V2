package com.tendersaucer.collector.events;

/**
 * Created by Alex on 5/5/2016.
 */
public abstract class Event<L> {

    public Event() {
    }

    public abstract void notify(L listener);
}
