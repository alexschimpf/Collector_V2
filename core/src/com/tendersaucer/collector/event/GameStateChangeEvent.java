package com.tendersaucer.collector.event;

import com.tendersaucer.collector.GameState;

/**
 * Created by Alex on 7/22/2016.
 */
public class GameStateChangeEvent extends Event<IGameStateChangeListener> {

    private GameState oldEvent;
    private GameState newEvent;

    public GameStateChangeEvent(GameState oldEvent, GameState newEvent) {
        this.oldEvent = oldEvent;
        this.newEvent = newEvent;
    }

    @Override
    public void notify(IGameStateChangeListener listener) {
        listener.onGameStateChange(oldEvent, newEvent);
    }
}
