package com.tendersaucer.collector.event;

/**
 * Interface for listening to world load end events
 * <p/>
 * Created by Alex on 4/23/2016.
 */
public interface IWorldLoadEndListener {

    /**
     * Fired by World after loading is complete (including entry room)
     */
    void onWorldLoadEnd();
}
