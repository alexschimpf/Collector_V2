package com.tendersaucer.collector;

import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.particle.ParticleEffect;
import com.tendersaucer.collector.particle.ParticleEffectManager;
import com.tendersaucer.collector.world.Room;
import com.tendersaucer.collector.world.World;

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
    public static final boolean PACK_TEXTURES = false;
    public static final short PLAYER_COLLISION_MASK = 0x0002;
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

    public static ParticleEffect getParticleEffect(String key, float x, float y) {
        return ParticleEffectManager.getInstance().buildParticleEffect(key, x, y);
    }

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
