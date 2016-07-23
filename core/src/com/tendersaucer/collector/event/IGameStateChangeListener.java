package com.tendersaucer.collector.event;

import com.tendersaucer.collector.GameState;

/**
 * Created by Alex on 7/22/2016.
 */
public interface IGameStateChangeListener {

    void onGameStateChange(GameState oldEvent, GameState newEvent);
}
