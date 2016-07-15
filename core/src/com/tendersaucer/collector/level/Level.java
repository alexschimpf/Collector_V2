package com.tendersaucer.collector.level;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.entity.EntityFactory;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.entity.RenderedEntity;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.LevelLoadBeginEvent;
import com.tendersaucer.collector.event.LevelLoadEndEvent;
import com.tendersaucer.collector.gen.EntityConstants;
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
    public static final float LOAD_DURATION = 500;
    private static final Level instance = new Level();

    private int id;
    private Long loadStartTime;
    private Player player;
    private World physicsWorld;
    private Vector2 respawnPosition;
    private final Map<String, Entity> entityMap;

    private Level() {
        entityMap = new ConcurrentHashMap<String, Entity>();

        respawnPosition = new Vector2();

        World.setVelocityThreshold(0.5f);
        physicsWorld = new World(new Vector2(0, DEFAULT_GRAVITY), true);
        physicsWorld.setContactListener(CollisionListener.getInstance());
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
        loadStartTime = TimeUtils.millis();
        EventManager.getInstance().notify(new LevelLoadBeginEvent(loadable));
        Globals.setGameState(Globals.GameState.LOADING);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                loadStartTime = null;
                EventManager.getInstance().postNotify(new LevelLoadEndEvent());
                Globals.setGameState(Globals.GameState.RUNNING);
            }
        }, LOAD_DURATION / 1000);

        id = loadable.getId();
        respawnPosition.set(loadable.getRespawnPosition());

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
    }

    public void loadNext() {
        load(new TiledMapLevelLoadable(getNextLevelId()));
    }

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    public Array<Body> getBodies() {
        Array<Body> bodies = new Array<Body>();
        physicsWorld.getBodies(bodies);

        return bodies;
    }

    // TODO: When does a room have no next room?
    public Integer getNextLevelId() {
        return id + 1;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public void addEntity(String id, Entity entity) {
        entityMap.put(id, entity);

        if (id.equals(EntityConstants.PLAYER)) {
            player = (Player)entity;
        }
    }

    public void removeEntity(String id) {
        entityMap.remove(id);

        if (id.equals(EntityConstants.PLAYER)) {
            player = null;
        }
    }

    public Entity getEntity(String id) {
        return entityMap.get(id);
    }

    public int getId() {
        return id;
    }

    public Vector2 getRespawnPosition() {
        return respawnPosition;
    }

    public String getAvailableEntityId() {
        String id;
        do {
            id = String.valueOf(MathUtils.random(0, Integer.MAX_VALUE));
        } while (entityMap.containsKey(id));

        return id;
    }

    public Long getLoadStartTime() {
        return loadStartTime;
    }

    private void loadEntities(ILevelLoadable loadable) {
        for (EntityDefinition entityDefinition : loadable.getEntityDefinitions()) {
            String id = entityDefinition.getId();
            if (id != null && entityMap.containsKey(id)) {
                throw new InvalidConfigException("Duplicate entity id: " + id);
            }

            Entity entity = EntityFactory.buildEntity(entityDefinition);
            if (Entity.isPlayer(entity)) {
                player = (Player)entity;
            }
            if (entity instanceof RenderedEntity) {
                ((RenderedEntity)entity).addToCanvas();
            }

            addEntity(entity.getId(), entity);
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
