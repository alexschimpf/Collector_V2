package com.tendersaucer.collector.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.tendersaucer.collector.Game;
import com.tendersaucer.collector.Globals;

public class DesktopLauncher {

	private static final String TEXTURE_PACK_NAME = "game";
	private static final String TEXTURES_DIR =  "/Users/schimpf1/Desktop/libgdx/Collector/assets/textures";
	private static final String DESTINATION_DIR = "/Users/schimpf1/Desktop/libgdx/Collector/android/assets";

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.resizable = false;
		config.title = "Collector";

		if(Globals.PACK_TEXTURES) {
			TexturePacker.Settings settings = new Settings();
			settings.duplicatePadding = true;
			TexturePacker.process(settings, TEXTURES_DIR, DESTINATION_DIR, "game");
		} else {
			new LwjglApplication(new Game(), config);
		}
	}
}
