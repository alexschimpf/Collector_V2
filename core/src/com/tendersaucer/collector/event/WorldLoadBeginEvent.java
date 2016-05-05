package com.tendersaucer.collector.event;

/**
 * Created by Alex on 5/5/2016.
 */
public class WorldLoadBeginEvent extends Event<IWorldLoadBeginListener> {

    @Override
    public void notify(IWorldLoadBeginListener listener) {
        listener.onWorldLoadBegin();
    }
}
