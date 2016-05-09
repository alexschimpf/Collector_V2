package com.tendersaucer.collector.event;

/**
 * Created by Alex on 5/5/2016.
 */
public class PlayerInteractionEvent extends Event<IPlayerInteractionListener> {

    @Override
    public void notify(IPlayerInteractionListener listener) {
        listener.onPlayerInteraction();
    }
}
