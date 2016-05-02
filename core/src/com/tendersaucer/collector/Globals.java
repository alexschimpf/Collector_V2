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

    public enum State {
        RUNNING, PAUSED, LOADING
    }

    public static final boolean ENABLE_MUSIC = true;
    public static final boolean FULLSCREEN_MODE = true;
    public static final boolean DEBUG_PHYSICS = false;
    public static final boolean PRINT_FPS = false;
    public static final boolean PACK_TEXTURES = true;
    public static final boolean SHOW_PARTICLE_EFFECT_VIEWER = true;
    public static State state;

    private Globals() {
        state = State.RUNNING;
    }

    public static com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
        return World.getInstance().getPhysicsWorld();
    }

    public static Player getPlayer() {
        return Room.getInstance().getPlayer();
    }

    public static float getTileSize() {
        return Camera.getInstance().getViewportWidth() / Camera.SCREEN_NUM_TILES_WIDE;
    }

//    public static Sprite getSprite(String textureKey) {
//        return getTextureManager().getSprite(textureKey);
//    }
//
//    public static TextureRegion getImageTexture(String textureKey) {
//        return getTextureManager().getImageTexture(textureKey);
//    }
//
//    public static TextureRegion getImageTexture(String textureKey, int index) {
//        return getTextureManager().getImageTexture(textureKey, index);
//    }
//
//    public static Array<AtlasRegion> getAnimationTextures(String animationKey) {
//        return getTextureManager().getAnimationTextures(animationKey);
//    }

    public static boolean isGameRunning() {
        return state == State.RUNNING;
    }

    public static boolean isGamePaused() {
        return state == State.PAUSED;
    }

    public static boolean isGameLoading() {
        return state == State.LOADING;
    }
}
