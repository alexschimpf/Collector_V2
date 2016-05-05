package com.tendersaucer.collector.events;

/**
 * Created by Alex on 5/5/2016.
 */
public class RoomLoadBeginEvent extends Event<IRoomLoadBeginListener> {

    @Override
    public void notify(IRoomLoadBeginListener listener) {
        listener.onRoomLoadBegin();
    }
}
