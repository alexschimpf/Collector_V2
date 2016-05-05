package com.tendersaucer.collector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.animation.Animation;

/**
 * Created by Alex on 5/5/2016.
 */
public final class AssetManager extends com.badlogic.gdx.assets.AssetManager {

    private static final AssetManager instance = new AssetManager();
    private static final String TEXTURES_DIR = "textures";
    private static final String SOUNDS_DIR = "sounds";
    private static final String TEXTURE_FORMAT = "png";
    private static final String TEXTURE_ATLAS_FORMAT = "atlas";
    private static final String SOUND_FORMAT = "mp3";

    private AssetManager() {
    }

    public static AssetManager getInstance() {
        return instance;
    }

    public void loadTextureAtlas(String name) {
        load(buildPath(TEXTURES_DIR, name, TEXTURE_ATLAS_FORMAT), TextureAtlas.class);
    }

    public void loadTexture(String name) {
        load(buildPath(TEXTURES_DIR, name, TEXTURE_FORMAT), Texture.class);
    }

    public void loadSound(String name) {
        load(buildPath(SOUNDS_DIR, name, SOUND_FORMAT), Sound.class);
    }

    public void loadTextures() {
        FileHandle dir = Gdx.files.internal(TEXTURES_DIR);
        for (FileHandle textureFile : dir.list()) {
            loadTexture(textureFile.name());
        }
    }

    public void loadSounds() {
        FileHandle dir = Gdx.files.internal(SOUNDS_DIR);
        for (FileHandle soundFile : dir.list()) {
            loadSound(soundFile.name());
        }
    }

    public void unloadTextureAtlas(String name) {
        unload(buildPath(TEXTURES_DIR, name, TEXTURE_ATLAS_FORMAT));
    }

    public void unloadTexture(String name) {
        unload(buildPath(TEXTURES_DIR, name, TEXTURE_FORMAT));
    }

    public void unloadSound(String name) {
        unload(buildPath(SOUNDS_DIR, name, SOUND_FORMAT));
    }

    public TextureAtlas getTextureAtlas(String name) {
        return get(buildPath(TEXTURES_DIR, name, TEXTURE_ATLAS_FORMAT), TextureAtlas.class);
    }

    public TextureRegion getTextureAtlasRegion(String atlasName, String regionName) {
        TextureAtlas textureAtlas = getTextureAtlas(atlasName);
        return textureAtlas.findRegion(regionName);
    }

    public Animation getTextureAtlasAnimation(String atlasName, String animationName) {
        TextureAtlas textureAtlas = getTextureAtlas(atlasName);
        Array<TextureAtlas.AtlasRegion> textureRegions = textureAtlas.findRegions(animationName);
        // TODO: Need to finish Animation class
        return new Animation();
    }

    public TextureRegion getTextureRegion(String name) {
        return get(buildPath(TEXTURES_DIR, name, TEXTURE_FORMAT), TextureRegion.class);
    }

    public Sound getSound(String name) {
        return get(buildPath(SOUNDS_DIR, name, SOUND_FORMAT), Sound.class);
    }

    private String buildPath(String basePath, String name, String format) {
        return basePath + "/" + name + "." + format;
    }
}
