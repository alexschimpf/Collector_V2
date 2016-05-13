package com.tendersaucer.collector.event;

import com.tendersaucer.collector.world.room.IRoomLoadable;

/**
 * Created by Alex on 5/5/2016.
 */
public class RoomLoadBeginEvent extends Event<IRoomLoadBeginListener> {

    private IRoomLoadable loadable;

    public RoomLoadBeginEvent(IRoomLoadable loadable) {
        this.loadable = loadable;
    }

    @Override
    public void notify(IRoomLoadBeginListener listener) {
        listener.onRoomLoadBegin(loadable);
    }
}
