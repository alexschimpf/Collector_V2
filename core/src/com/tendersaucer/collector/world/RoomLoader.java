package com.tendersaucer.collector.world;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.Layers;

import java.util.Map;

/**
 * Loads rooms from config
 *
 * Created by Alex on 4/8/2016.
 */
public final class RoomLoader {

    private static final RoomLoader instance = new RoomLoader();

    private final Array<IRoomChangedListener> roomChangedListeners;

    private RoomLoader() {
        roomChangedListeners = new Array<IRoomChangedListener>();
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

    public void load(String worldId, String roomId) {
        Layers layers = Layers.getInstance();
        layers.clearLayers();

        IRoomLoadable roomLoadable = new TiledMapRoomLoadable(worldId, roomId);

        Map<IRender, Integer> renderableLayerMap = roomLoadable.getRenderableLayerMap();
        for (IRender object : renderableLayerMap.keySet()) {
            int layer = renderableLayerMap.get(object);
            layers.addToLayer(layer, object);
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
