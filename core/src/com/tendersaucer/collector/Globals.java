package com.tendersaucer.collector;

import com.tendersaucer.collector.asset.MusicManager;
import com.tendersaucer.collector.asset.SoundManager;
import com.tendersaucer.collector.asset.TextureManager;
import com.tendersaucer.collector.entity.EntityPropertyValidator;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.particle.ParticleEffect;
import com.tendersaucer.collector.particle.ParticleEffectManager;
import com.tendersaucer.collector.ui.HUD;
import com.tendersaucer.collector.util.Vector2Pool;
import com.tendersaucer.collector.world.Room;
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

    public static HUD getHUD() {
        return HUD.getInstance();
    }

    public static World getWorld() {
        return World.getInstance();
    }

    public static Room getCurrentRoom() {
        return getWorld().getCurrentRoom();
    }

    public static com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
        return getWorld().getPhysicsWorld();
    }

    public static Camera getCamera() {
        return Camera.getInstance();
    }

    public static Layers getLayers() {
        return Layers.getInstance();
    }

    public static TextureManager getTextureManager() {
        return TextureManager.getInstance();
    }

    public static SoundManager getSoundManager() {
        return SoundManager.getInstance();
    }

    public static MusicManager getMusicManager() {
        return MusicManager.getInstance();
    }

    public static ParticleEffectManager getParticleEffectManager() {
        return ParticleEffectManager.getInstance();
    }

    public static Vector2Pool getVector2Pool() {
        return Vector2Pool.getInstance();
    }

    public static EntityPropertyValidator getEntityPropertyValidator() {
        return EntityPropertyValidator.getInstance();
    }

    public static Player getPlayer() {
        return getWorld().getPlayer();
    }

    public static float getTileSize() {
        return getCamera().getViewportWidth() / Camera.SCREEN_NUM_TILES_WIDE;
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
        return getParticleEffectManager().buildParticleEffect(key, x, y);
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
