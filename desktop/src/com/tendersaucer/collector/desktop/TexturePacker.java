package com.tendersaucer.collector.desktop;

/**
 * Created by Alex on 5/26/2016.
 */
public class TexturePacker {

    private static final String TEXTURE_PACK_NAME = "0";
    private static final String TEXTURES_DIR =  "/Users/Alex/Desktop/libgdx/Collector/android/assets/texture_atlas/textures";
    private static final String DESTINATION_DIR = "/Users/Alex/Desktop/libgdx/Collector/android/assets/texture_atlas";


    private TexturePacker() {
    }

    public static void main(String[] args) {
        com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings settings =
                new com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings();
        settings.duplicatePadding = true;
        com.badlogic.gdx.tools.texturepacker.TexturePacker.process(settings, TEXTURES_DIR,
                DESTINATION_DIR, TEXTURE_PACK_NAME);
    }
}
