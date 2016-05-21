package com.tendersaucer.collector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.animation.AnimatedSprite;
import com.tendersaucer.collector.event.IWorldLoadBeginListener;
import com.tendersaucer.collector.world.IWorldLoadable;
import com.tendersaucer.collector.world.World;

import java.nio.file.Paths;

/**
 * Created by Alex on 5/5/2016.
 */
public final class AssetManager extends com.badlogic.gdx.assets.AssetManager implements IWorldLoadBeginListener {

    private static final AssetManager instance = new AssetManager();
    private static final String DEFAULT_WORLD_ID = "0";
    private static final String TEXTURE_ATLAS_DIR = "texture_atlas";
    private static final String SOUND_DIR = "sound";
    private static final String TEXTURE_ATLAS_EXTENSION = ".atlas";
    private static final String SOUND_EXTENSION = ".wav";

    private AssetManager() {
    }

    public static AssetManager getInstance() {
        return instance;
    }

    public static String getFilePath(String first, String... more) {
        return Paths.get(first, more).toString().replace("\\", "/");
    }

    @Override
    public void onWorldLoadBegin(IWorldLoadable loadable) {
        load(loadable.getId());
    }

    public void load(String worldId) {
        unloadWorldAssets();
        loadWorldAssets(worldId);
        finishLoading();
    }

    /**
     * Uses the world's id as the atlas name
     * @param regionId
     * @return
     */
    public TextureRegion getTextureRegion(String regionId) {
        return getTextureAtlasRegion(getWorldId(), regionId);
    }

    /**
     * Uses the world's id as the atlas name
     * @param regionId
     * @return
     */
    public Array<AtlasRegion> getTextureRegions(String regionId) {
        return getTextureAtlasRegions(getWorldId(), regionId);
    }

    public Sprite getSprite(String regionId) {
        return new Sprite(getTextureRegion(regionId));
    }

    public AnimatedSprite getAnimatedSprite(String regionId) {
        return new AnimatedSprite(regionId);
    }

    public AnimatedSprite getAnimatedSprite(String regionId, float totalDuration) {
        return new AnimatedSprite(regionId, totalDuration);
    }

    public AnimatedSprite getAnimatedSprite(String regionId, float totalDuration, Integer numLoops) {
        return new AnimatedSprite(regionId, totalDuration, numLoops);
    }

    public Sound getSound(String id) {
        String fileName = getFilePath(SOUND_DIR, id + SOUND_EXTENSION);
        if (!isLoaded(fileName)) {
            Gdx.app.log("assets", "Asset '" + fileName + "' has not been loaded");
        }

        return get(fileName, Sound.class);
    }

    private void unloadWorldAssets() {
        unloadTextureAtlas(getWorldId());
        unloadSounds();
    }

    private void loadWorldAssets(String worldId) {
        loadTextureAtlas(worldId);
        loadSounds();
    }

    private void loadTextureAtlas(String id) {
        load(getFilePath(TEXTURE_ATLAS_DIR, id + TEXTURE_ATLAS_EXTENSION), TextureAtlas.class);
    }

    private void unloadTextureAtlas(String id) {
        String fileName = getFilePath(TEXTURE_ATLAS_DIR, id + TEXTURE_ATLAS_EXTENSION);
        if (isLoaded(fileName)) {
            unload(fileName);
        }
    }

    private void loadSound(String id) {
        load(getFilePath(SOUND_DIR, id + SOUND_EXTENSION), Sound.class);
    }

    private void unloadSound(String id) {
        String fileName = getFilePath(SOUND_DIR, id + SOUND_EXTENSION);
        if (isLoaded(fileName)) {
            unload(fileName);
        }
    }

    private void loadSounds() {
        FileHandle dir = Gdx.files.internal(SOUND_DIR);
        for (FileHandle soundFile : dir.list()) {
            loadSound(soundFile.nameWithoutExtension());
        }
    }

    private void unloadSounds() {
        FileHandle dir = Gdx.files.internal(SOUND_DIR);
        for (FileHandle soundFile : dir.list()) {
            unloadSound(soundFile.nameWithoutExtension());
        }
    }

    private TextureAtlas getTextureAtlas(String id) {
        String fileName = getFilePath(TEXTURE_ATLAS_DIR, id + TEXTURE_ATLAS_EXTENSION);
        if (!isLoaded(fileName)) {
            Gdx.app.log("assets", "Asset '" + fileName + "' has not been loaded");
        }

        return get(fileName, TextureAtlas.class);
    }

    private TextureRegion getTextureAtlasRegion(String atlasName, String regionId) {
        TextureAtlas textureAtlas = getTextureAtlas(atlasName);
        return textureAtlas.findRegion(regionId);
    }

    private Array<AtlasRegion> getTextureAtlasRegions(String atlasName, String regionId) {
        TextureAtlas textureAtlas = getTextureAtlas(atlasName);
        return textureAtlas.findRegions(regionId);
    }

    private String getWorldId() {
        String worldId = World.getInstance().getId();
        if (worldId == null) {
            // e.g. when no world exists for ParticleEffectViewer
            worldId = DEFAULT_WORLD_ID;
        }
        
        return worldId;
    }
}
