package com.tendersaucer.collector.tools;

import com.badlogic.gdx.Gdx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String TEXTURES_DIR = "/Users/Alex/Desktop/libgdx/Collector/android/assets/texture_atlas/textures";
    private static final String OUTPUT_DIR = "/Users/Alex/Desktop/libgdx/Collector/android/assets/worlds/0/rooms/";

    private TilesetGenerator() {
    }

    public static void generate(String fileName) {
        int i = 0, page = 0;
        File[] files = getTextureFiles();
        while (i < files.length) {
            BufferedImage image = new BufferedImage(TILESET_SIZE, TILESET_SIZE,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setBackground(java.awt.Color.RED);
            graphics.clearRect(0, 0, TILESET_SIZE, TILESET_SIZE);

            try {
                for (int row = 0; row < NUM_TILES_TALL; row++) {
                    for (int col = 0; col < NUM_TILES_WIDE; col++) {
                        if (i >= files.length) {
                            break;
                        }

                        File textureFile = files[i++];
                        BufferedImage textureImage = ImageIO.read(textureFile);
                        int x = (col * (TILE_SIZE + SPACING)) + SPACING;
                        int y = (row * (TILE_SIZE + SPACING)) + SPACING;
                        graphics.drawImage(textureImage, x, y, null);
                    }
                }

                String outputFileName = OUTPUT_DIR + fileName + "_" + page++ + ".png";
                ImageIO.write(image, "png", new File(outputFileName));
            } catch (Exception e) {
                Gdx.app.log("tileset", e.toString());
            }
        }
    }

    private static File[] getTextureFiles() {
        File[] files = new File("C:/" + TEXTURES_DIR).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png");
            }
        });

        final Pattern pattern = Pattern.compile("(.+)_(\\d+)\\.png");
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                Matcher aMatcher = pattern.matcher(a.getName());
                Matcher bMatcher = pattern.matcher(b.getName());

                // e.g. If A="xxx_0.png" and B="xxx_1.png", put A first.
                if (aMatcher.matches() && bMatcher.matches()) {
                    String aName = aMatcher.group(1);
                    String bName = bMatcher.group(1);
                    if (aName.equals(bName)) {
                        int aIndex = Integer.parseInt(aMatcher.group(2));
                        int bIndex = Integer.parseInt(bMatcher.group(2));
                        return aIndex - bIndex;
                    }
                }

                return a.getName().compareTo(b.getName());
            }
        });

        return files;
    }
}
