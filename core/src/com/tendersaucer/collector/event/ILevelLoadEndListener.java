package com.tendersaucer.collector.event;

/**
 * Interface for listening to level load end events
 * <p/>
 * Created by Alex on 4/23/2016.
 */
public interface ILevelLoadEndListener {

    /**
     * Fired by Level after loading is complete
     */
    void onLevelLoadEnd();
}
