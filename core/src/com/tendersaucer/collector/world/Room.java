package com.tendersaucer.collector.world;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.Canvas;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.util.InvalidConfigException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Levels of a world
 *
 * Created by Alex on 4/8/2016.
 */
public final class Room implements IUpdate {

    private static final Room instance = new Room();

    private Player player;
    private final Map<String, Entity> entityMap;
    private final Array<IRoomLoadBeginListener> roomLoadBeginListeners;
    private final Array<IRoomLoadEndListener> roomLoadEndListeners;

    private Room() {
        entityMap = new ConcurrentHashMap<String, Entity>();
        roomLoadBeginListeners = new Array<IRoomLoadBeginListener>();
        roomLoadEndListeners = new Array<IRoomLoadEndListener>();
    }

    public static Room getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        return false;
    }

    public void load(IRoomLoadable roomLoadable) {
        notifyRoomLoadBeginListeners();

        // Add entities to Canvas.
        Map<IRender, Integer> renderableLayerMap = roomLoadable.getRenderableLayerMap();
        for (IRender object : renderableLayerMap.keySet()) {
            int layer = renderableLayerMap.get(object);
            Canvas.getInstance().addToLayer(layer, object);
        }

        entityMap.clear();

        for (Entity entity : roomLoadable.getEntities()) {
            String id = entity.getId();
            if (entityMap.containsKey(id)) {
                throw new InvalidConfigException("Duplicate entity id: " + id);
            }

            entityMap.put(id, entity);
            if (Entity.isPlayer(entity)) {
                setPlayer((Player)entity);
            }
        }

        notifyRoomLoadEndListeners();
    }

    public Player getPlayer() {
        return player;
    }

    public void clearRoomLoadBeginListeners() {
        roomLoadBeginListeners.clear();
    }

    public void addRoomLoadBeginListener(IRoomLoadBeginListener listener) {
        roomLoadBeginListeners.add(listener);
    }

    public void removeRoomLoadBeginListener(IRoomLoadBeginListener listener) {
        roomLoadBeginListeners.removeValue(listener, true);
    }

    public void clearRoomLoadEndListeners() {
        roomLoadEndListeners.clear();
    }

    public void addRoomLoadEndListener(IRoomLoadEndListener listener) {
        roomLoadEndListeners.add(listener);
    }

    public void removeRoomLoadEndListener(IRoomLoadEndListener listener) {
        roomLoadEndListeners.removeValue(listener, true);
    }

    private void setPlayer(Player player) {
        this.player = player;
    }

    private void notifyRoomLoadBeginListeners() {
        for (IRoomLoadBeginListener listener : roomLoadBeginListeners) {
            listener.onRoomLoadBegin();
        }
    }

    private void notifyRoomLoadEndListeners() {
        for (IRoomLoadEndListener listener : roomLoadEndListeners) {
            listener.onRoomLoadEnd();
        }
    }
}
