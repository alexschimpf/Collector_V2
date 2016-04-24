package com.tendersaucer.collector.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.CollisionListener;
import com.tendersaucer.collector.IUpdate;

import java.util.Iterator;

/**
 * Manages rooms
 *
 * Created by Alex on 4/8/2016.
 */
public final class World implements IUpdate {

    public static final float DEFAULT_GRAVITY = 20;
    private static final World instance = new World();

    private String id;
    private String entryRoomId;
    private final com.badlogic.gdx.physics.box2d.World physicsWorld;
    private final Array<IWorldLoadBeginListener> worldLoadBeginListeners;
    private final Array<IWorldLoadEndListener> worldLoadEndListeners;

    private World() {
        physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, DEFAULT_GRAVITY), true);

        com.badlogic.gdx.physics.box2d.World.setVelocityThreshold(0.5f);
        physicsWorld.setContactListener(CollisionListener.getInstance());

        worldLoadBeginListeners = new Array<IWorldLoadBeginListener>();
        worldLoadEndListeners = new Array<IWorldLoadEndListener>();
    }

    public static World getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        physicsWorld.step(1 / 45.0f, 5, 5);
        Room.getInstance().update();

        return false;
    }

    public void load(IWorldLoadable loadable) {
        notifyWorldLoadBeginListeners();

        id = loadable.getId();
        entryRoomId = loadable.getEntryRoomId();

        clearPhysicsWorld();
        Room.getInstance().load(new TiledMapRoomLoadable(id, entryRoomId));

        notifyWorldLoadEndListeners();
    }

    public com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
        return physicsWorld;
    }

    public Array<Body> getBodies() {
        Array<Body> bodies = new Array<Body>();
        physicsWorld.getBodies(bodies);

        return bodies;
    }

    public String getId() {
        return id;
    }

    public String getEntryRoomId() {
        return entryRoomId;
    }

    public void clearWorldLoadBeginListeners() {
        worldLoadBeginListeners.clear();
    }

    public void addWorldLoadBeginListener(IWorldLoadBeginListener listener) {
        worldLoadBeginListeners.add(listener);
    }

    public void removeWorldLoadBeginListener(IWorldLoadBeginListener listener) {
        worldLoadBeginListeners.removeValue(listener, true);
    }

    public void clearWorldLoadEndListeners() {
        worldLoadEndListeners.clear();
    }

    public void addWorldLoadEndListener(IWorldLoadEndListener listener) {
        worldLoadEndListeners.add(listener);
    }

    public void removeWorldLoadEndListener(IWorldLoadEndListener listener) {
        worldLoadEndListeners.removeValue(listener, true);
    }

    private void clearPhysicsWorld() {
        Iterator<Body> bodiesIter = getBodies().iterator();
        while (bodiesIter.hasNext()) {
            Body body = bodiesIter.next();
            physicsWorld.destroyBody(body);

            bodiesIter.remove();
        }
    }

    private void notifyWorldLoadBeginListeners() {
        for (IWorldLoadBeginListener listener : worldLoadBeginListeners) {
            listener.onWorldLoadBegin();
        }
    }

    private void notifyWorldLoadEndListeners() {
        for (IWorldLoadEndListener listener : worldLoadEndListeners) {
            listener.onWorldLoadEnd();
        }
    }
}
