package com.tendersaucer.collector.world;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.Canvas;
import com.tendersaucer.collector.FixtureBodyDefinition;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.entity.EntityFactory;
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

    private String id;
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

        id = roomLoadable.getId();

        // TODO: roomLoadable.getBackground();

        // Add non-entity/background canvas objects.
        Map<IRender, Integer> canvasMap = roomLoadable.getCanvasMap();
        for (IRender object : canvasMap.keySet()) {
            int layer = canvasMap.get(object);
            Canvas.getInstance().addToLayer(layer, object);
        }

        entityMap.clear();

        // Add new entities.
        for (EntityDefinition entityDefinition : roomLoadable.getEntityDefinitions()) {
            Entity entity = EntityFactory.buildEntity(entityDefinition);

            String id = entity.getId();
            if (entityMap.containsKey(id)) {
                throw new InvalidConfigException("Duplicate entity id: " + id);
            }

            entityMap.put(id, entity);
            if (Entity.isPlayer(entity)) {
                setPlayer((Player)entity);
            }

            // Add entity to canvas.
            Canvas.getInstance().addToLayer(entityDefinition.getLayer(), entity);
        }

        // Add free bodies.
        for (FixtureBodyDefinition fixtureBodyDef : roomLoadable.getFreeBodyDefinitions()) {
            Body body = Globals.getPhysicsWorld().createBody(fixtureBodyDef.bodyDef);
            body.createFixture(fixtureBodyDef.fixtureDef);

            fixtureBodyDef.fixtureDef.shape.dispose();
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

    public String getId() {
        return id;
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
