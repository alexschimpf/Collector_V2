package com.tendersaucer.collector.event;

/**
 * Created by Alex on 5/5/2016.
 */
public class LevelLoadEndEvent extends Event<ILevelLoadEndListener> {

    @Override
    public void notify(ILevelLoadEndListener listener) {
        listener.onLevelLoadEnd();
    }
}
