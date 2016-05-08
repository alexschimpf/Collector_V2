package com.tendersaucer.collector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Alex on 5/5/2016.
 */
public final class AssetManager extends com.badlogic.gdx.assets.AssetManager {

    private static final AssetManager instance = new AssetManager();
    private static final String TEXTURE_DIR = "textures";
    private static final String TEXTURE_ATLAS_DIR = "texture_atlases";
    private static final String SOUND_DIR = "sounds";
    private static final String TEXTURE_EXTENSION = "png";
    private static final String TEXTURE_ATLAS_EXTENSION = "atlas";
    private static final String SOUND_EXTENSION = "mp3";

    private AssetManager() {
    }

    public static AssetManager getInstance() {
        return instance;
    }

    public void loadTextureAtlas(String name) {
        load(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_ATLAS_EXTENSION, TEXTURE_ATLAS_DIR, name), TextureAtlas.class);
    }

    public void loadTexture(String name) {
        load(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_EXTENSION, TEXTURE_DIR, name), Texture.class);
    }

    public void loadSound(String name) {
        load(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                SOUND_EXTENSION, SOUND_DIR, name), Sound.class);
    }

    public void loadTextures() {
        FileHandle dir = Gdx.files.internal(TEXTURE_DIR);
        for (FileHandle textureFile : dir.list()) {
            if (textureFile.extension().equals(TEXTURE_EXTENSION)) {
                loadTexture(textureFile.nameWithoutExtension());
            }
        }
    }

    public void loadSounds() {
        FileHandle dir = Gdx.files.internal(SOUND_DIR);
        for (FileHandle soundFile : dir.list()) {
            loadSound(soundFile.nameWithoutExtension());
        }
    }

    public void unloadTextureAtlas(String name) {
        unload(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_ATLAS_EXTENSION, TEXTURE_DIR, name));
    }

    public void unloadTexture(String name) {
        unload(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_EXTENSION, TEXTURE_DIR, name));
    }

    public void unloadSound(String name) {
        unload(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                SOUND_EXTENSION, SOUND_DIR, name));
    }

    public TextureAtlas getTextureAtlas(String name) {
        return get(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_ATLAS_EXTENSION, TEXTURE_ATLAS_DIR, name), TextureAtlas.class);
    }

    public TextureRegion getTextureAtlasRegion(String atlasName, String regionName) {
        TextureAtlas textureAtlas = getTextureAtlas(atlasName);
        return textureAtlas.findRegion(regionName);
    }

    public Array<AtlasRegion> getTextureAtlasRegions(String atlasName, String regionName) {
        TextureAtlas textureAtlas = getTextureAtlas(atlasName);
        return textureAtlas.findRegions(regionName);
    }

    public TextureRegion getTextureRegion(String name) {
        return get(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                TEXTURE_EXTENSION, TEXTURE_DIR, name), TextureRegion.class);
    }

    public Sound getSound(String name) {
        return get(com.tendersaucer.collector.util.FileUtils.buildFilePathWithExtension(
                SOUND_EXTENSION, SOUND_DIR, name), Sound.class);
    }
}
