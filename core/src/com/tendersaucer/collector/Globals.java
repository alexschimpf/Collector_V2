package com.tendersaucer.collector;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

/**
 * Game global variables
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class Globals {

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

    public enum GameState {
        RUNNING, PAUSED, LOADING
    }
}
