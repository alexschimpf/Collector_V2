package com.tendersaucer.collector.world;

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
    private final Map<String, Entity> entityMap = new ConcurrentHashMap<String, Entity>();

    private Room() {
    }

    public static Room getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void onDone() {
    }

    public void set(IRoomLoadable roomLoadable) {
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
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
