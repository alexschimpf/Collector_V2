package com.tendersaucer.collector.events;

/**
 * Interface for listening to room load end events
 *
 * Created by Alex on 4/23/2016.
 */
public interface IRoomLoadEndListener {

    /**
     * Fired by Room after loading is complete
     */
    void onRoomLoadEnd();
}
