package com.tendersaucer.collector.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.tendersaucer.collector.Game;
import com.tendersaucer.collector.Globals;

public class DesktopLauncher {

	private static final String TEXTURE_PACK_NAME = "textures";
	private static final String TEXTURES_DIR =  "/Users/Alex/Desktop/libgdx/Collector/assets/textures";
	private static final String DESTINATION_DIR = "/Users/Alex/Desktop/libgdx/Collector/android/assets/textures";

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		//config.fullscreen = Globals.FULLSCREEN_MODE;
		config.resizable = false;
		config.title = "Collector";

		if (!config.fullscreen) {
			config.width = 1280;
			config.height = 1280;
		}

		if(Globals.PACK_TEXTURES) {
			TexturePacker.Settings settings = new Settings();
			settings.duplicatePadding = true;
			TexturePacker.process(settings, TEXTURES_DIR, DESTINATION_DIR, TEXTURE_PACK_NAME);
		} else {
			new LwjglApplication(new Game(), config);
		}
	}
}
