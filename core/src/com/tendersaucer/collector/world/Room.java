package com.tendersaucer.collector.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
public final class Room implements IUpdate, IRender {

    private static final Room instance = new Room();

    private final Map<String, Entity> entityMap = new ConcurrentHashMap<String, Entity>();

    private Room() {
    }

    public static Room getInstance() {
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

    public void set(IRoomLoadable roomLoadable) {
        for (Entity entity : roomLoadable.getEntities()) {
            String id = entity.getId();
            if (entityMap.containsKey(id)) {
                throw new InvalidConfigException("Duplicate entity id: " + id);
            }

            entityMap.put(id, entity);
            if (Entity.isPlayer(entity)) {
                World.getInstance().setPlayer((Player)entity);
            }
        }
    }
}
