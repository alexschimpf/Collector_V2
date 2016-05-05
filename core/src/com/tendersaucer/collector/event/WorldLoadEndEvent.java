package com.tendersaucer.collector.event;

/**
 * Created by Alex on 5/5/2016.
 */
public class WorldLoadEndEvent extends Event<IWorldLoadEndListener> {

    @Override
    public void notify(IWorldLoadEndListener listener) {
        listener.onWorldLoadEnd();
    }
}
