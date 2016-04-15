package com.tendersaucer.collector;

import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.particle.ParticleEffect;
import com.tendersaucer.collector.particle.ParticleEffectManager;
import com.tendersaucer.collector.world.World;

/**
 * Game global variables
 *
 * Created by Alex on 4/8/2016.
 */
public final class Globals {

    public static boolean ENABLE_MUSIC = true;
    public static boolean FULLSCREEN_MODE = true;
    public static boolean DEBUG_PHYSICS = false;
    public static boolean PRINT_FPS = false;
    public static boolean PACK_TEXTURES = false;

    public enum State {
        RUNNING, PAUSED, LOADING
    };

    public static State state = State.RUNNING;

    public static final short PLAYER_COLLISION_MASK = 0x0002;

    private Globals() {
    }

    public static com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
        return World.getInstance().getPhysicsWorld();
    }

    public static Player getPlayer() {
        return World.getInstance().getPlayer();
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
