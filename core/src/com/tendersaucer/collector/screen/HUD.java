package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tendersaucer.collector.IUpdate;

/**
 * Game heads up display
 *
 * Created by Alex on 4/8/2016.
 */
public final class HUD implements IUpdate, IRender {

    private static final HUD instance = new HUD();

    private Stage stage;

    private HUD() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.addListener(new InputListener());
        Gdx.input.setInputProcessor(stage);
    }

    public static HUD getInstance() {
        return instance;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
       stage.draw();
    }

    @Override
    public boolean update() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        return false;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }
}
