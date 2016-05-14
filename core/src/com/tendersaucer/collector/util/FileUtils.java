package com.tendersaucer.collector.util;

/**
 * File / config utility functions
 *
 * Created by Alex on 4/8/2016.
 */
public final class FileUtils {

    private static final String ROOMS_DIR = "rooms";
    private static final String WORLDS_DIR = "worlds";
    private static final String WORLD_CONFIG_NAME = "world.xml";

    private FileUtils() {
    }

    public static String getWorldConfigURI(String worldId) {
        return buildFilePath(WORLDS_DIR, worldId, WORLD_CONFIG_NAME);
    }

    public static String getRoomConfigURI(String worldId, String roomId) {
        return buildFilePath(WORLDS_DIR, worldId, ROOMS_DIR, roomId + ".tmx");
    }

    public static String buildFilePath(String... parts) {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            path.append(parts[i]);
            if (i < parts.length - 1) {
                path.append("/");
            }
        }

        return path.toString();
    }

    public static String buildFilePathWithExtension(String extension, String... parts) {
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            path.append(parts[i]);
            if (i < parts.length - 1) {
                path.append("/");
            }
        }

        return path.append(".").append(extension).toString();
    }
}
