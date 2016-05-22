package com.tendersaucer.collector.tools;

import com.badlogic.gdx.Gdx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Created by Alex on 5/21/2016.
 */
public class TilesetGenerator {

    private static final int SPACING = 1;
    private static final int TILE_SIZE = 64; // assume square
    private static final int TILESET_SIZE = 1024;
    private static final int NUM_TILES_WIDE = TILESET_SIZE / (TILE_SIZE + (SPACING * 2));
    private static final int NUM_TILES_TALL = NUM_TILES_WIDE;
    private static final String TEXTURES_DIR =  "/Users/Alex/Desktop/libgdx/Collector/android/assets/texture_atlas/textures";
    private static final String OUTPUT_FILE = "/Users/Alex/Desktop/libgdx/Collector/android/assets/worlds/0/rooms/";

    private TilesetGenerator() {
    }

    public static void generate(String fileName) {
        int i = 0;
        File[] files = new File("C:/" + TEXTURES_DIR).listFiles();
        while (i < files.length) {
            BufferedImage image = new BufferedImage(TILESET_SIZE, TILESET_SIZE,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setBackground(java.awt.Color.RED);
            graphics.clearRect(0, 0, TILESET_SIZE, TILESET_SIZE);

            for (int row = 0; row < NUM_TILES_TALL; row++) {
                for (int col = 0; col < NUM_TILES_WIDE; col++) {
                    int x = (col * (TILE_SIZE + SPACING)) + SPACING;
                    int y = (row * (TILE_SIZE + SPACING)) + SPACING;

                    if (i >= files.length) {
                        break;
                    }

                    File textureFile = files[i++];
                    BufferedImage textureImage = null;
                    try {
                        textureImage = ImageIO.read(textureFile);
                    } catch (Exception e) {
                        Gdx.app.log("tileset", e.toString());
                        return;
                    }

                    graphics.drawImage(textureImage, x, y, null);
                }
            }

            try {
                File outputfile = new File(OUTPUT_FILE + fileName);
                ImageIO.write(image, "png", outputfile);
            } catch (Exception e) {
                Gdx.app.log("tileset", e.toString());
            }
        }
    }
}
