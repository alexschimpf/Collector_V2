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

/**
 * Created by Alex on 5/5/2016.
 */
public final class AssetManager extends com.badlogic.gdx.assets.AssetManager implements IWorldLoadBeginListener {

    private static final AssetManager instance = new AssetManager();
    private static final String TEXTURE_ATLAS_DIR = "texture_atlases";
    private static final String SOUND_DIR = "sounds";
    private static final String TEXTURE_ATLAS_EXTENSION = "atlas";
    private static final String SOUND_EXTENSION = "mp3";

    private AssetManager() {
    }

    public static AssetManager getInstance() {
        return instance;
    }

    @Override
    public void onWorldLoadBegin(IWorldLoadable loadable) {
        unloadWorldAssets();
        loadWorldAssets(loadable.getId());
        finishLoading();
    }

    /**
     * Uses the world's id as the atlas name
     * @param regionName
     * @return
     */
    public TextureRegion getTextureRegion(String regionName) {
        return getTextureAtlasRegion(World.getInstance().getId(), regionName);
    }

    /**
     * Uses the world's id as the atlas name
     * @param regionName
     * @return
     */
    public Array<AtlasRegion> getTextureRegions(String regionName) {
        return getTextureAtlasRegions(World.getInstance().getId(), regionName);
    }

    public Sprite getSprite(String regionName) {
        return new Sprite(getTextureRegion(regionName));
    }

    public AnimatedSprite getAnimatedSprite(String regionName) {
        return new AnimatedSprite(regionName);
    }

    public AnimatedSprite getAnimatedSprite(String regionName, float totalDuration) {
        return new AnimatedSprite(regionName, totalDuration);
    }

    public AnimatedSprite getAnimatedSprite(String regionName, float totalDuration, Integer numLoops) {
        return new AnimatedSprite(regionName, totalDuration, numLoops);
    }

    public Sound getSound(String name) {
        return get(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                SOUND_EXTENSION, SOUND_DIR, name), Sound.class);
    }

    private void unloadWorldAssets() {
        unloadTextureAtlas(World.getInstance().getId());
        unloadSounds();
    }

    private void loadWorldAssets(String worldId) {
        loadTextureAtlas(worldId);
        loadSounds();
    }

    private void loadTextureAtlas(String name) {
        load(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_ATLAS_EXTENSION, TEXTURE_ATLAS_DIR, name), TextureAtlas.class);
    }

    private void unloadTextureAtlas(String name) {
        unload(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_ATLAS_EXTENSION, TEXTURE_ATLAS_DIR, name));
    }

    private void loadSound(String name) {
        load(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                SOUND_EXTENSION, SOUND_DIR, name), Sound.class);
    }

    private void unloadSound(String name) {
        unload(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                SOUND_EXTENSION, SOUND_DIR, name));
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

    private TextureAtlas getTextureAtlas(String name) {
        return get(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_ATLAS_EXTENSION, TEXTURE_ATLAS_DIR, name), TextureAtlas.class);
    }

    private TextureRegion getTextureAtlasRegion(String atlasName, String regionName) {
        TextureAtlas textureAtlas = getTextureAtlas(atlasName);
        return textureAtlas.findRegion(regionName);
    }

    private Array<AtlasRegion> getTextureAtlasRegions(String atlasName, String regionName) {
        TextureAtlas textureAtlas = getTextureAtlas(atlasName);
        return textureAtlas.findRegions(regionName);
    }

}
