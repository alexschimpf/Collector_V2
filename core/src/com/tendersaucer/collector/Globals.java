package com.tendersaucer.collector;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.GameStateChangeEvent;

/**
 * Game global variables
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class Globals {

    public static final int NUM_LEVELS = 1;
    public static boolean FULLSCREEN_MODE = true;
    public static boolean DEBUG_PHYSICS = false;
    public static boolean PRINT_DEBUG_INFO = false;
    public static boolean CUSTOM_CAMERA_MODE = false;
    public static boolean CLEAR_PREFERENCES = false;
    public static int LOG_LEVEL = Application.LOG_ERROR;
    private static GameState gameState;

    private Globals() {
        Gdx.app.setLogLevel(LOG_LEVEL);
        gameState = GameState.RUNNING;
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

    public static void  setGameState(GameState gameState) {
        GameState oldGameState = Globals.gameState;
        Gdx.app.debug("Globals", "Game state changed from '" + oldGameState + "' to '" + gameState + "'");

        Globals.gameState = gameState;
        EventManager.getInstance().notify(new GameStateChangeEvent(oldGameState, gameState));
    }

    public static GameState getGameState() {
        return gameState;
    }
}
