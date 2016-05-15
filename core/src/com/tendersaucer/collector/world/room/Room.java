package com.tendersaucer.collector.world.room;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.entity.EntityFactory;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.entity.VisibleEntity;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.RoomLoadBeginEvent;
import com.tendersaucer.collector.event.RoomLoadEndEvent;
import com.tendersaucer.collector.screen.Canvas;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.util.EntityUtils;
import com.tendersaucer.collector.util.FixtureBodyDefinition;
import com.tendersaucer.collector.util.InvalidConfigException;

import java.util.Iterator;
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

    private Room() {
        entityMap = new ConcurrentHashMap<String, Entity>();
    }

    public static Room getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        Iterator<String> entityIdIter = entityMap.keySet().iterator();
        while (entityIdIter.hasNext()) {
            Entity entity = entityMap.get(entityIdIter.next());
            if (entity.update()) {
                entity.dispose();
                entityIdIter.remove();
            }
        }

        return false;
    }

    public void load(IRoomLoadable loadable) {
        EventManager.getInstance().notify(new RoomLoadBeginEvent(loadable));

        id = loadable.getId();

        Canvas.getInstance().addToLayer(0, loadable.getBackground());

        // Add non-entity/background canvas objects.
        Map<IRender, Integer> canvasMap = loadable.getCanvasMap();
        for (IRender object : canvasMap.keySet()) {
            int layer = canvasMap.get(object);
            Canvas.getInstance().addToLayer(layer, object);
        }

        entityMap.clear();
        loadEntities(loadable);
        loadFreeBodies(loadable);

        EventManager.getInstance().notify(new RoomLoadEndEvent());
    }

    public Player getPlayer() {
        return player;
    }

    public String getId() {
        return id;
    }

    public String getAvailableEntityId() {
        String id;
        do {
            id = String.valueOf(MathUtils.random(0, Integer.MAX_VALUE));
        } while (entityMap.containsKey(id));

        return id;
    }

    private void loadEntities(IRoomLoadable loadable) {
        for (EntityDefinition entityDefinition : loadable.getEntityDefinitions()) {
            Entity entity = EntityFactory.buildEntity(entityDefinition);

            String id = entity.getId();
            if (entityMap.containsKey(id)) {
                throw new InvalidConfigException("Duplicate entity id: " + id);
            }

            entityMap.put(id, entity);
            if (EntityUtils.isPlayer(entity)) {
                setPlayer((Player)entity);
            }

            if (entity instanceof VisibleEntity) {
                entity.addToCanvas();
            }
        }
    }

    private void loadFreeBodies(IRoomLoadable loadable) {
        for (FixtureBodyDefinition fixtureBodyDef : loadable.getFreeBodyDefinitions()) {
            Body body = Globals.getPhysicsWorld().createBody(fixtureBodyDef.bodyDef);
            body.createFixture(fixtureBodyDef.fixtureDef);

            fixtureBodyDef.fixtureDef.shape.dispose();
        }
    }

    private void setPlayer(Player player) {
        this.player = player;
    }
}
