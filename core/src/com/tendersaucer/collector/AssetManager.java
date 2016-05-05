package com.tendersaucer.collector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Alex on 5/5/2016.
 */
public final class AssetManager extends com.badlogic.gdx.assets.AssetManager {

    private static final String TEXTURES_DIR = "textures/";
    private static final String SOUNDS_DIR = "sounds/";
    private static final String TEXTURE_FORMAT = ".png";
    private static final String TEXTURE_ATLAS_FORMAT = ".atlas";
    private static final String SOUND_FORMAT = ".mp3";

    public void loadTextureAtlas(String name) {
        load(TEXTURES_DIR + name + TEXTURE_ATLAS_FORMAT, TextureAtlas.class);
    }

    public void loadTexture(String name) {
        load(TEXTURES_DIR + name + TEXTURE_FORMAT, Texture.class);
    }

    public void loadSound(String name) {
        load(SOUNDS_DIR + name + SOUND_FORMAT, Sound.class);
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
        unload(TEXTURES_DIR + name + TEXTURE_ATLAS_FORMAT);
    }

    public void unloadTexture(String name) {
        unload(TEXTURES_DIR + name + TEXTURE_FORMAT);
    }

    public void unloadSound(String name) {
        unload(SOUNDS_DIR + name + SOUND_FORMAT);
    }
}
