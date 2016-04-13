package com.tendersaucer.collector.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.entity.Player;

import java.util.Iterator;

/**
 * Manages rooms
 *
 * Created by Alex on 4/8/2016.
 */
public final class World implements IUpdate, IRender {

    private static final World instance = new World();

    private World() {
    }

    public static World getInstance() {
        return instance;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void onDone() {
    }

    public void clearPhysicsWorld() {
        Iterator<Body> bodiesIter = getBodies().iterator();
        while(bodiesIter.hasNext()) {
            Body body = bodiesIter.next();
            getPhysicsWorld().destroyBody(body);
        }
    }

    public com.badlogic.gdx.physics.box2d.World getPhysicsWorld() {
        return null;
    }

    public Array<Body> getBodies() {
        return getPhysicsWorld().getBodies();
    }

    public Player getPlayer() {
        return null;
    }
}
