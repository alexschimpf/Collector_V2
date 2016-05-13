package com.tendersaucer.collector.event;

import com.tendersaucer.collector.world.IWorldLoadable;

/**
 * Interface for listening to world load begin events
 *
 * Created by Alex on 4/23/2016.
 */
public interface IWorldLoadBeginListener {

    /**
     * Fired by World before any loading occurs
     */
    void onWorldLoadBegin(IWorldLoadable loadable);
}
