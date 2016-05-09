package com.tendersaucer.collector.event;

/**
 * Interface for listening to room load beginning events
 *
 * Created by Alex on 4/23/2016.
 */
public interface IRoomLoadBeginListener {

    /**
     * Fired by Room before any loading occurs
     */
    void onRoomLoadBegin();
}
