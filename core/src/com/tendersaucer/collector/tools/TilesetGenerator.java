package com.tendersaucer.collector.tools;

import com.badlogic.gdx.Gdx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

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
    private static final String OUTPUT_DIR = "/Users/Alex/Desktop/libgdx/Collector/android/assets/worlds/0/rooms/";

    private TilesetGenerator() {
    }

    public static void generate(String fileName) {
        int i = 0;
        int page = 0;
        File[] files = getTextureFiles();
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
                File outputfile = new File(OUTPUT_DIR + fileName + "_" + page + ".png");
                ImageIO.write(image, "png", outputfile);
            } catch (Exception e) {
                Gdx.app.log("tileset", e.toString());
            }

            page++;
        }
    }

    private static File[] getTextureFiles() {
        File[] files = new File("C:/" + TEXTURES_DIR).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png");
            }
        });

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                String aFullName = a.getName();
                String bFullName = b.getName();
                if (aFullName.contains("_") && bFullName.contains("_")) {
                    int aIndexStart = aFullName.lastIndexOf("_") + 1;
                    int bIndexStart = bFullName.lastIndexOf("_") + 1;
                    String aName = aFullName.substring(0, aIndexStart - 1);
                    String bName = bFullName.substring(0, bIndexStart - 1);
                    if (aName.equals(bName)) {
                        String aIndex = aFullName.substring(aIndexStart).replace(".png", "");
                        String bIndex = bFullName.substring(bIndexStart).replace(".png", "");
                        if (isInteger(aIndex) && isInteger(bIndex)) {
                            return Integer.parseInt(aIndex) - Integer.parseInt(bIndex);
                        }
                    }
                }

                return aFullName.compareTo(bFullName);
            }
        });

        return files;
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
