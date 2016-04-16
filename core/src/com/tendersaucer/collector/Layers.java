package com.tendersaucer.collector;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Layers of IRender objects
 *
 * Created by Alex on 4/10/2016.
 */
public final class Layers implements IRender {

    private static final Layers instance = new Layers();

    public static final int NUM_LAYERS = 10;

    // 0 = Background, 10 = Foreground
    private final ConcurrentHashMap<IRender, Integer> objectLayerMap;
    private final Array<LinkedHashMap<IRender, Boolean>> layers;

    private Layers() {
        objectLayerMap = new ConcurrentHashMap<IRender, Integer>();
        layers = new Array<LinkedHashMap<IRender, Boolean>>();
        for (int i = 0; i < layers.size; i++) {
            layers.set(i, new LinkedHashMap<IRender, Boolean>());
        }
    }

    public static Layers getInstance() {
        return instance;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (int i = 0; i < layers.size; i++) {
            LinkedHashMap<IRender, Boolean> layer = layers.get(i);
            for (IRender object : layer.keySet()) {
                object.render(spriteBatch);
            }
        }
    }

    public void clearLayers() {
        layers.clear();
        objectLayerMap.clear();
    }

    public void clearLayer(int layer) {
        checkLayer(layer);
        layers.get(layer).clear();

        Iterator objectLayerMapIter = objectLayerMap.keySet().iterator();
        while (objectLayerMapIter.hasNext()) {
            IRender object = (IRender)objectLayerMapIter.next();
            int objectLayer = objectLayerMap.get(object);
            if (objectLayer == layer) {
                objectLayerMapIter.remove();
            }
        }
    }

    public void addToLayer(int layer, IRender object) {
        checkLayer(layer);
        layers.get(layer).put(object, true);
        objectLayerMap.put(object, layer);
    }

    public void remove(IRender object) {
        int layer = objectLayerMap.get(object);
        layers.get(layer).remove(object);
        objectLayerMap.remove(object);
    }

    public int getLayer(IRender object) {
        return objectLayerMap.get(object);
    }

    private void checkLayer(int layer) {
        if (layer < 0 || layer >= NUM_LAYERS) {
            throw new IndexOutOfBoundsException("Layer " + layer + " is out of bounds");
        }
    }
}
