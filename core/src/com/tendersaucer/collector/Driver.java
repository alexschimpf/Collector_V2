package com.tendersaucer.collector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.tendersaucer.collector.world.WorldLoader;

/**
 * Main update and render logic
 *
 * Created by Alex on 4/8/2016.
 */
public class Driver implements Screen {

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

        Layers layers = Globals.getLayers();
        layers.addToLayer(1, Globals.getParticleEffectManager());
        layers.addToLayer(2, Globals.getWorld());

        WorldLoader.load("0");
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
        Globals.getCamera().update();
        Globals.getWorld().update();
        Globals.getHUD().update();
        Globals.getParticleEffectManager().update();
    }

    private void render(){
        Gdx.gl.glClearColor((240 / 255.0f), (250 / 255.0f), (250 / 255.0f), 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = (OrthographicCamera)Globals.getCamera().getRawCamera();

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
