package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tendersaucer.collector.GameState;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.event.IGameStateChangeListener;
import com.tendersaucer.collector.level.Level;

/**
 * Game heads up display
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class HUD implements IUpdate, IRender, IGameStateChangeListener {

    private static final HUD instance = new HUD();

    private Stage stage;
    private InputListener inputListener;
    private Button nextButton;
    private Button replayButton;

    private HUD() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        inputListener = new InputListener();
        stage.addListener(inputListener);
        Gdx.input.setInputProcessor(stage);

        createLevelCompleteButtons();
        hideLevelCompleteButtons();
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
        if (Globals.isDesktop()) {
            inputListener.update();
        } else {
            // TODO
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        return false;
    }

    @Override
    public void onGameStateChange(GameState oldEvent, GameState newEvent) {
        if (newEvent == GameState.LEVEL_COMPLETE) {
            Gdx.app.log("HUD", "Showing level completion dialog...");
            showLevelCompleteButtons();
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public InputListener getInputListener() {
        return inputListener;
    }

    private void hideLevelCompleteButtons() {
        nextButton.setDisabled(true);
        replayButton.setDisabled(true);
        nextButton.setVisible(false);
        replayButton.setVisible(false);
    }

    private void showLevelCompleteButtons() {
        nextButton.setDisabled(false);
        replayButton.setDisabled(false);
        nextButton.setVisible(true);
        replayButton.setVisible(true);
    }

    private void createLevelCompleteButtons() {
        if (Level.getInstance().getPlayer() != null){
            Level.getInstance().getPlayer().setDone();
        }

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        float buttonSpacing = screenWidth * .1f;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = (int)(screenWidth / 10);
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = generator.generateFont(parameter);
        buttonStyle.fontColor = Color.BLACK;

        float nextButtonWidth = screenWidth * 0.75f;
        float nextButtonHeight = screenHeight * 0.35f;
        float nextButtonX = (screenWidth - nextButtonWidth) / 2;
        float nextButtonY = buttonSpacing + nextButtonHeight;
        nextButton = new TextButton("next", skin);
        nextButton.setSize(nextButtonWidth, nextButtonHeight);
        nextButton.setPosition(nextButtonX, nextButtonY);
        nextButton.setStyle(buttonStyle);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hideLevelCompleteButtons();
                Level.getInstance().loadNext();
            }
        });

        float replayButtonWidth = screenWidth * 0.55f;
        float replayButtonHeight = screenHeight * 0.25f;
        float replayButtonX = (screenWidth - replayButtonWidth) / 2;
        float replayButtonY = nextButtonY - buttonSpacing - replayButtonHeight;
        replayButton = new TextButton("replay", skin);
        replayButton.setSize(replayButtonWidth, replayButtonHeight);
        replayButton.setPosition(replayButtonX, replayButtonY);
        replayButton.setStyle(buttonStyle);
        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hideLevelCompleteButtons();
                Level.getInstance().replay();
            }
        });

        stage.addActor(replayButton);
        stage.addActor(nextButton);
    }
}
