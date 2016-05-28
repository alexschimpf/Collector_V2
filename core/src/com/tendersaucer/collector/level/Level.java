package com.tendersaucer.collector.level;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.entity.EntityFactory;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.entity.RenderedEntity;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.LevelLoadBeginEvent;
import com.tendersaucer.collector.event.LevelLoadEndEvent;
import com.tendersaucer.collector.screen.Canvas;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.util.FixtureBodyDefinition;
import com.tendersaucer.collector.util.InvalidConfigException;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A single level
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class Level implements IUpdate {

    public static final float DEFAULT_GRAVITY = 50;
    private static final Level instance = new Level();

    private World physicsWorld;

    private final Map<String, Entity> entityMap;
    private String id;
    private Player player;

    private Level() {
        entityMap = new ConcurrentHashMap<String, Entity>();

        com.badlogic.gdx.physics.box2d.World.setVelocityThreshold(0.5f);
        physicsWorld = new World(new Vector2(0, DEFAULT_GRAVITY), true);
        physicsWorld.setContactListener(com.tendersaucer.collector.level.CollisionListener.getInstance());
    }

    public static Level getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        physicsWorld.step(1 / 45.0f, 5, 5);

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

    public void load(ILevelLoadable loadable) {
        EventManager.getInstance().notify(new LevelLoadBeginEvent(loadable));

        id = loadable.getId();

        clearPhysicsWorld();
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

        EventManager.getInstance().notify(new LevelLoadEndEvent());
    }

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    public Array<Body> getBodies() {
        Array<Body> bodies = new Array<Body>();
        physicsWorld.getBodies(bodies);

        return bodies;
    }

    public Player getPlayer() {
        return player;
    }

    private void setPlayer(Player player) {
        this.player = player;
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

    private void loadEntities(ILevelLoadable loadable) {
        for (EntityDefinition entityDefinition : loadable.getEntityDefinitions()) {
            Entity entity = EntityFactory.buildEntity(entityDefinition);

            String id = entity.getId();
            if (entityMap.containsKey(id)) {
                throw new InvalidConfigException("Duplicate entity id: " + id);
            }

            entityMap.put(id, entity);
            if (Entity.isPlayer(entity)) {
                setPlayer((Player)entity);
            }

            if (entity instanceof RenderedEntity) {
                ((RenderedEntity)entity).addToCanvas();
            }
        }
    }

    private void loadFreeBodies(ILevelLoadable loadable) {
        for (FixtureBodyDefinition fixtureBodyDef : loadable.getFreeBodyDefinitions()) {
            Body body = physicsWorld.createBody(fixtureBodyDef.bodyDef);
            body.createFixture(fixtureBodyDef.fixtureDef);

            fixtureBodyDef.fixtureDef.shape.dispose();
        }
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