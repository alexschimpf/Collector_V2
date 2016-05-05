package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.Camera;
import com.tendersaucer.collector.Canvas;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.particle.ParticleEffectManager;
import com.tendersaucer.collector.util.ConversionUtils;

/**
 * Created by Alex on 4/30/2016.
 */
public class ParticleEffectViewer implements Screen {

    private Stage stage;
    private String selectedEffectType;
    private final Skin skin;
    private final SpriteBatch spriteBatch;

    public ParticleEffectViewer() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        if (Globals.FULLSCREEN_MODE) {
            // TODO: Display mode hack
        }

        // TODO: Load things world-by-world.
        // TODO: Do loading asynchronously.
        AssetManager assetManager = AssetManager.getInstance();
        // assetManager.loadSounds();
        // assetManager.loadTextures();
        assetManager.loadTextureAtlas("textures");
        assetManager.finishLoading();

        ParticleEffectManager.getInstance().loadDefinitions();

        setStage();
        createDropdown();
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
        stage.getViewport().update(width, height, false);
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
        ParticleEffectManager.getInstance().update();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    }

    private void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = (OrthographicCamera)Camera.getInstance().getRawCamera();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin(); {
            Canvas.getInstance().render(spriteBatch);
        } spriteBatch.end();
    }

    private void setStage() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (selectedEffectType != null) {
                    Vector2 position = ConversionUtils.toWorldCoords(x, y);
                    Vector2 sizeRange = new Vector2(Gdx.graphics.getWidth() / 60,
                            Gdx.graphics.getHeight() / 30);
                    ParticleEffectManager.getInstance().beginParticleEffect(selectedEffectType,
                            position, sizeRange, 5);
                }

                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    private void createDropdown() {
        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();

        final SelectBox<String> dropdown = new SelectBox<String>(skin);
        dropdown.setSize(screenWidth, screenHeight / 8);

        float padding = screenWidth / 10;
        dropdown.setPosition(padding, screenHeight - dropdown.getHeight() - padding);

        dropdown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedEffectType = dropdown.getSelected();
            }
        });

        // Load dropdown.
        Array<String> effectTypes = ParticleEffectManager.getInstance().getParticleEffectTypes();
        dropdown.setItems(effectTypes);

        stage.addActor(dropdown);
    }
}
