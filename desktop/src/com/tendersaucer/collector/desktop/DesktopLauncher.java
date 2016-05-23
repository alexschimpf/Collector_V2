package com.tendersaucer.collector.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.tendersaucer.collector.Game;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.tools.TilesetGenerator;

public class DesktopLauncher {

	private static final String TEXTURE_PACK_NAME = "0";
	private static final String TEXTURES_DIR =  "/Users/Alex/Desktop/libgdx/Collector/android/assets/texture_atlas/textures";
	private static final String DESTINATION_DIR = "/Users/Alex/Desktop/libgdx/Collector/android/assets/texture_atlas";

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.fullscreen = Globals.FULLSCREEN_MODE;
		config.resizable = false;
		config.title = "Collector";

		// TODO: How to get native resolution?
		if (Globals.FULLSCREEN_MODE) {
			config.width = 3200;
			config.height = 1800;
		} else {
			config.width = 1280;
			config.height = 720;
		}

		if(Globals.PACK_TEXTURES) {
			TexturePacker.Settings settings = new Settings();
			settings.duplicatePadding = true;
			TexturePacker.process(settings, TEXTURES_DIR, DESTINATION_DIR, TEXTURE_PACK_NAME);
		} else if (Globals.PACK_TILESETS) {
			TilesetGenerator.generate("entity_tiles");
		} else {
			new LwjglApplication(new Game(), config);
		}
	}
}
