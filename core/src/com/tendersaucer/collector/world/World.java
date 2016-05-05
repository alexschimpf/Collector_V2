package com.tendersaucer.collector.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.CollisionListener;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.events.EventManager;
import com.tendersaucer.collector.events.WorldLoadBeginEvent;
import com.tendersaucer.collector.events.WorldLoadEndEvent;
import com.tendersaucer.collector.world.room.Room;
import com.tendersaucer.collector.world.room.TiledMapRoomLoadable;

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

    private World() {
        physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, DEFAULT_GRAVITY), true);

        com.badlogic.gdx.physics.box2d.World.setVelocityThreshold(0.5f);
        physicsWorld.setContactListener(CollisionListener.getInstance());
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
        EventManager.getInstance().notify(WorldLoadBeginEvent.class);

        id = loadable.getId();
        entryRoomId = loadable.getEntryRoomId();

        clearPhysicsWorld();
        Room.getInstance().load(new TiledMapRoomLoadable(id, entryRoomId));

        EventManager.getInstance().notify(WorldLoadEndEvent.class);
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

    private void clearPhysicsWorld() {
        Iterator<Body> bodiesIter = getBodies().iterator();
        while (bodiesIter.hasNext()) {
            Body body = bodiesIter.next();
            physicsWorld.destroyBody(body);

            bodiesIter.remove();
        }
    }
}
