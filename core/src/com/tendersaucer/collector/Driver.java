package com.tendersaucer.collector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.particle.ParticleEffect;

/**
 * Main update and render logic
 *
 * Created by Alex on 4/8/2016.
 */
public class Driver implements Screen {

    private static final int NUM_LAYERS = 10;

    private final Matrix4 debugMatrix = new Matrix4();
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    public Driver() {
    }

    @Override
    public void show() {
        if (Globals.FULLSCREEN_MODE) {
            // TODO: Display mode hack
        }

        // TODO: Determine last world
        // TODO: Load entry room
    }

    @Override
    public void render(float delta) {
        if(Globals.PRINT_FPS) {
            Gdx.app.log("FPS", "" + Gdx.graphics.getFramesPerSecond());
        }

        Globals.getCamera().update();

        update();
        render();
    }

    @Override
    public void resize(int width, int height) {
        Globals.getCamera().resizeViewport(width, height);
        Globals.getHUD().resize(width, height);
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
        Globals.getGameWorld().update();
        Globals.getHUD().update();

        // TODO: Update ParticleEffects
    }

    private void render(){
        Gdx.gl.glClearColor((240 / 255.0f), (250 / 255.0f), (250 / 255.0f), 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = Globals.getCamera().getRawCamera();

        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin(); {
            Globals.getLayers().render(spriteBatch);
        } spriteBatch.end();

        if(Globals.DEBUG_PHYSICS) {
            debugMatrix.set(camera.combined);
            debugRenderer.render(Globals.getPhysicsWorld(), debugMatrix);
        }

        Globals.getHUD().render(spriteBatch);
    }
}
