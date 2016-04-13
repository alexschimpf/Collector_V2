package com.tendersaucer.collector.world;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.Layers;

/**
 * Loads rooms from config
 *
 * Created by Alex on 4/8/2016.
 */
public final class RoomLoader {

    private final static Array<IRoomChangedListener> roomChangedListeners = new Array<IRoomChangedListener>();

    private RoomLoader() {
    }

    public static void addRoomChangedListener(IRoomChangedListener listener) {
        roomChangedListeners.add(listener);
    }

    public static void removeRoomChangedListener(IRoomChangedListener listener) {
        roomChangedListeners.removeValue(listener, true);
    }

    public static void load(IRoomLoadable roomLoadable) {
        for (int i = 0; i < Layers.NUM_LAYERS; i++) {
            if (i != Layers.PARTICLE_LAYER && i != Layers.WORLD_LAYER) {
                Globals.getLayers().clearLayer(i);
            }
        }

        int i = 0;
        for (IRender layer : roomLoadable.getRenderLayers()) {
            Globals.getLayers().addToLayer(i++, layer);
        }

        Globals.getRoom().set(roomLoadable);

        notifyRoomChangedListeners();
    }

    private static void notifyRoomChangedListeners() {
        for (IRoomChangedListener listener : roomChangedListeners) {
            listener.onRoomChanged();
        }
    }
}
