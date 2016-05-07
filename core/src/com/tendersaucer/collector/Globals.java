package com.tendersaucer.collector;

import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.world.World;
import com.tendersaucer.collector.world.room.Room;

/**
 * Game global variables
 *
 * Created by Alex on 4/8/2016.
 */
public final class Globals {

    public enum GameState {
        RUNNING, PAUSED, LOADING
    }

    public static boolean ENABLE_MUSIC = false;
    public static boolean FULLSCREEN_MODE = false;
    public static boolean DEBUG_PHYSICS = true;
    public static boolean PRINT_DEBUG_INFO = false;
    public static boolean PACK_TEXTURES = false;
    public static boolean SHOW_PARTICLE_EFFECT_VIEWER = false;

    private static GameState gameState;

    private Globals() {
        gameState = GameState.RUNNING;
    }

    public static com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
        return World.getInstance().getPhysicsWorld();
    }

    public static Player getPlayer() {
        return Room.getInstance().getPlayer();
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static boolean isGameRunning() {
        return gameState == GameState.RUNNING;
    }

    public static boolean isGamePaused() {
        return gameState == GameState.PAUSED;
    }

    public static boolean isGameLoading() {
        return gameState == GameState.LOADING;
    }
}
