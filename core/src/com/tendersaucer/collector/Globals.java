package com.tendersaucer.collector;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
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
    public static boolean DEBUG_PHYSICS = false;
    public static boolean PRINT_DEBUG_INFO = false;
    public static boolean PACK_TEXTURES = false;
    public static boolean SHOW_PARTICLE_EFFECT_VIEWER = false;

    private static GameState gameState;

    private Globals() {
        gameState = GameState.RUNNING;
    }

    public static ApplicationType getApplicationType() {
        return Gdx.app.getType();
    }

    public static boolean isAndroid() {
        return Gdx.app.getType().equals(ApplicationType.Android);
    }

    public static boolean isDesktop() {
        return Gdx.app.getType().equals(ApplicationType.Desktop);
    }

    public static boolean isIOS() {
        return Gdx.app.getType().equals(ApplicationType.iOS);
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
