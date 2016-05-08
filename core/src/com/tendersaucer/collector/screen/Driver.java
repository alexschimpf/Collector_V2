package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.MainCamera;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.RoomLoadBeginEvent;
import com.tendersaucer.collector.particle.ParticleEffectManager;
import com.tendersaucer.collector.util.Debug;
import com.tendersaucer.collector.world.IWorldLoadable;
import com.tendersaucer.collector.world.World;
import com.tendersaucer.collector.world.XMLWorldLoadable;

/**
 * Main update and render logic
 *
 * Created by Alex on 4/8/2016.
 */
public final class Driver implements Screen {

    public static Driver instance = new Driver();

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
        // TODO: Load world-by-world.
        // TODO: Load asynchronously.
        AssetManager assetManager = AssetManager.getInstance();
        assetManager.loadSounds();
        assetManager.loadTextures();
        assetManager.loadTextureAtlas("textures");
        assetManager.finishLoading();

        ParticleEffectManager.getInstance().loadDefinitions();

        EventManager eventManager = EventManager.getInstance();
        eventManager.listen(RoomLoadBeginEvent.class, Canvas.getInstance());
        eventManager.listen(RoomLoadBeginEvent.class, ParticleEffectManager.getInstance());

        IWorldLoadable worldLoadable = new XMLWorldLoadable("0");
        World.getInstance().load(worldLoadable);
    }

    @Override
    public void render(float delta) {
        if(Globals.PRINT_DEBUG_INFO) {
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    private void update() {
        MainCamera.getInstance().update();
        HUD.getInstance().update();
        World.getInstance().update();
        ParticleEffectManager.getInstance().update();
    }

    private void render(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = (OrthographicCamera) MainCamera.getInstance().getRawCamera();

        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin(); {
            Canvas.getInstance().render(spriteBatch);
        } spriteBatch.end();

        if(Globals.DEBUG_PHYSICS) {
            debugMatrix.set(camera.combined);
            debugRenderer.render(Globals.getPhysicsWorld(), debugMatrix);
        }

        HUD.getInstance().render(spriteBatch);
    }
}
