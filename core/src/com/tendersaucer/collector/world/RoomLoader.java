package com.tendersaucer.collector.world;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.Layers;

/**
 * Loads rooms from config
 *
 * Created by Alex on 4/8/2016.
 */
public final class RoomLoader {

    private static final RoomLoader instance = new RoomLoader();

    private final Array<IRoomChangedListener> roomChangedListeners = new Array<IRoomChangedListener>();

    private RoomLoader() {
    }

    public static RoomLoader getInstance() {
        return instance;
    }

    public void addRoomChangedListener(IRoomChangedListener listener) {
        roomChangedListeners.add(listener);
    }

    public void removeRoomChangedListener(IRoomChangedListener listener) {
        roomChangedListeners.removeValue(listener, true);
    }

    public void load(IRoomLoadable roomLoadable) {
        for (int i = 0; i < Layers.NUM_LAYERS; i++) {
            if (i != Layers.PARTICLE_LAYER && i != Layers.WORLD_LAYER) {
                Layers.getInstance().clearLayer(i);
            }
        }

        int i = 0;
        for (IRender layer : roomLoadable.getRenderLayers()) {
            Layers.getInstance().addToLayer(i++, layer);
        }

        Room.getInstance().set(roomLoadable);

        notifyRoomChangedListeners();
    }

    private void notifyRoomChangedListeners() {
        for (IRoomChangedListener listener : roomChangedListeners) {
            listener.onRoomChanged();
        }
    }
}
