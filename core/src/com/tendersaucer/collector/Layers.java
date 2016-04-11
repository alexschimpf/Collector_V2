package com.tendersaucer.collector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Layers of IRender objects
 *
 * Created by Alex on 4/10/2016.
 */
public final class Layers implements IRender {

    private static final int NUM_LAYERS = 10;
    private static final Layers instance = new Layers();

    // 0 = Background, 10 = Foreground
    private final Array<Array<IRender>> layers = new Array<Array<IRender>>(NUM_LAYERS);

    private Layers() {
        for (int i = 0; i < layers.size; i++) {
            layers.set(i, new Array<IRender>());
        }
    }

    public static Layers getInstance() {
        return instance;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (int i = 0; i < layers.size; i++) {
            Array<IRender> layer = layers.get(i);
            for (int j = 0; j < layer.size; j++) {
                layer.get(i).render(spriteBatch);
            }
        }
    }

    public void clearLayers() {
        for (int i = 0; i < layers.size; i++) {
            clearLayer(i);
        }
    }

    public void clearLayer(int layer) {
        layers.get(layer).clear();
    }

    public void addToLayer(int layer, IRender obj) {
        layers.get(layer).add(obj);
    }

    public void removeFromLayer(int layer, IRender obj) {
        layers.get(layer).removeValue(obj, true);
    }
}
