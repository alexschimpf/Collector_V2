package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.tendersaucer.collector.Camera;
import com.tendersaucer.collector.Canvas;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.particle.ParticleEffectManager;
import com.tendersaucer.collector.ui.HUD;
import com.tendersaucer.collector.world.IWorldLoadable;
import com.tendersaucer.collector.world.World;
import com.tendersaucer.collector.world.XMLWorldLoadable;
import com.tendersaucer.collector.world.room.Room;

/**
 * Main update and render logic
 *
 * Created by Alex on 4/8/2016.
 */
public final class Driver implements Screen {

    private final Matrix4 debugMatrix;
    private final SpriteBatch spriteBatch;
    private final Box2DDebugRenderer debugRenderer;

    public Driver() {
        debugMatrix = new Matrix4();
        spriteBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
        if (Globals.FULLSCREEN_MODE) {
            // TODO: Display mode hack
        }

        ParticleEffectManager.getInstance().loadDefinitions();

        Room room = Room.getInstance();
        room.addRoomLoadBeginListener(Canvas.getInstance());
        room.addRoomLoadBeginListener(ParticleEffectManager.getInstance());

        IWorldLoadable worldLoadable = new XMLWorldLoadable("0");
        World.getInstance().load(worldLoadable);
    }

    @Override
    public void render(float delta) {
        if(Globals.PRINT_FPS) {
            Gdx.app.log("FPS", "" + Gdx.graphics.getFramesPerSecond());
        }

        update();
        render();
    }

    @Override
    public void resize(int width, int height) {
        Camera.getInstance().resizeViewport(width, height);
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

    private void update() {
        Camera.getInstance().update();
        World.getInstance().update();
        HUD.getInstance().update();
        ParticleEffectManager.getInstance().update();
    }

    private void render(){
        Gdx.gl.glClearColor((240 / 255.0f), (250 / 255.0f), (250 / 255.0f), 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = (OrthographicCamera)Camera.getInstance().getRawCamera();

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
