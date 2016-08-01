package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.DAO;
import com.tendersaucer.collector.GameState;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.MainCamera;
import com.tendersaucer.collector.StatisticsListener;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.GameStateChangeEvent;
import com.tendersaucer.collector.event.LevelLoadBeginEvent;
import com.tendersaucer.collector.event.NewUserEvent;
import com.tendersaucer.collector.level.Level;
import com.tendersaucer.collector.particle.ParticleEffectManager;
import com.tendersaucer.collector.util.Debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main update and render logic
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class Driver implements Screen {

    public static final Driver instance = new Driver();

    private final Matrix4 debugMatrix;
    private final SpriteBatch spriteBatch;
    private final Box2DDebugRenderer debugRenderer;

    private Driver() {
        debugMatrix = new Matrix4();
        spriteBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
    }

    public static Driver getInstance() {
        return instance;
    }

    @Override
    public void show() {
        AssetManager.getInstance().load();
        ParticleEffectManager.getInstance().loadDefinitions();

        EventManager eventManager = EventManager.getInstance();
        eventManager.listen(LevelLoadBeginEvent.class, Canvas.getInstance());
        eventManager.listen(LevelLoadBeginEvent.class, ParticleEffectManager.getInstance());
        eventManager.listen(GameStateChangeEvent.class, HUD.getInstance());
        eventManager.listen(GameStateChangeEvent.class, StatisticsListener.getInstance());

        DAO dao = DAO.getInstance();
        long iterationId = dao.getIterationId();
        int levelId = (int)dao.getLevelId();
        Level.getInstance().load(iterationId, levelId);

        if (dao.isNew()) {
            eventManager.notify(new NewUserEvent());
            dao.putString(DAO.COLOR_ORDER_KEY, getRandomColorOrder());
            dao.putBoolean(DAO.IS_NEW_KEY, false);
        }
    }

    private String getRandomColorOrder() {
        List<String> codes = new ArrayList<String>();
        codes.add("r"); codes.add("g"); codes.add("b");
        Collections.shuffle(codes);

        String order = codes.get(0);
        for (int i = 1; i < codes.size(); i++) {
            order += "," + codes.get(i);
        }

        return order;
    }

    @Override
    public void render(float delta) {
        if (Globals.PRINT_DEBUG_INFO) {
            Debug.printDebugInfo();
        }

        update();
        render();
    }

    @Override
    public void resize(int width, int height) {
        MainCamera.getInstance().resizeViewport(width, height);
        HUD.getInstance().resize(width, height);
    }

    @Override
    public void pause() {
        Globals.setGameState(GameState.PAUSED);
    }

    @Override
    public void resume() {
        Level.getInstance().replay(); // then state becomes WAIT_FOR_INPUT
    }

    @Override
    public void hide() {
        Globals.setGameState(GameState.PAUSED);
    }

    @Override
    public void dispose() {
        Globals.setGameState(GameState.SHUTDOWN);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    private void update() {
        MainCamera.getInstance().update();
        HUD.getInstance().update();
        Level.getInstance().update();
        ParticleEffectManager.getInstance().update();
    }

    private void render() {
        if (Globals.PRINT_DEBUG_INFO) {
            Debug.printDebugInfo();
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = MainCamera.getInstance().getRawCamera();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin(); {
            Canvas.getInstance().render(spriteBatch);
        }
        spriteBatch.end();

        if (Globals.DEBUG_PHYSICS) {
            debugMatrix.set(camera.combined);
            debugRenderer.render(Level.getInstance().getPhysicsWorld(), debugMatrix);
        }

        HUD.getInstance().render(spriteBatch);
    }
}
