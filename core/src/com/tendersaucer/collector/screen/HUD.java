package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.GameState;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.event.IGameStateChangeListener;
import com.tendersaucer.collector.level.Level;
import com.tendersaucer.collector.statistics.StatisticsListener;

import java.util.concurrent.TimeUnit;

/**
 * Game heads up display
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class HUD implements IUpdate, IRender, IGameStateChangeListener {

    private static final HUD instance = new HUD();
    private static final float BUTTON_ALPHA = 0.5f;

    private Stage stage;
    private InputListener inputListener;
    private Image levelCompleteBackground;
    private Label levelSummaryLabel;
    private TextButton nextButton;
    private Label progressLabel;
    private Button moveButton;
    private Button jumpButton;
    private Integer movePointer;
    private FreeTypeFontGenerator fontGenerator;
    private Skin skin;

    private HUD() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        inputListener = new InputListener();
        stage.addListener(inputListener);
        Gdx.input.setInputProcessor(stage);

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        createProgressLabel();
        createLevelCompleteUI();
        hideLevelComplete();

        if (Globals.isAndroid() || Globals.isIOS()) {
            createMobileButtons();
        }
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
            checkMobileButtons();
        }

        StatisticsListener statisticsListener = StatisticsListener.getInstance();
        long iterationId = statisticsListener.getIterationId();
        long levelId = statisticsListener.getLevelId();
        long runId = statisticsListener.getRunId();
        progressLabel.setText(iterationId + "." + levelId + "." + runId);

        float labelHeight = progressLabel.getPrefHeight();
        float screenHeight = Gdx.graphics.getHeight();
        float margin = screenHeight / 50;
        progressLabel.setPosition(margin, screenHeight - (labelHeight / 2) - margin);
        levelSummaryLabel.setPosition(margin, margin + (labelHeight / 2));

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        return false;
    }

    @Override
    public void onGameStateChange(GameState oldEvent, GameState newEvent) {
        if (newEvent == GameState.LEVEL_COMPLETE) {
            Gdx.app.debug("HUD", "Showing level completion dialog...");

            // TODO: Hacky way to ensure statistics have been updated first.
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    showLevelComplete();
                }
            });
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public InputListener getInputListener() {
        return inputListener;
    }

    private void hideLevelComplete() {
        levelCompleteBackground.setVisible(false);
        nextButton.setDisabled(true);
        nextButton.setVisible(false);

        levelSummaryLabel.setText("");
        levelSummaryLabel.setVisible(false);
    }

    private void showLevelComplete() {
        levelCompleteBackground.setVisible(true);
        nextButton.setDisabled(false);
        nextButton.setVisible(true);

        long duration = StatisticsListener.getInstance().getTotalTime();
        levelSummaryLabel.setText("You spent " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds doing that...");
        levelSummaryLabel.setVisible(true);
    }

    private void createProgressLabel() {
        progressLabel = new Label("", skin);

        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = Gdx.graphics.getWidth() / 30;
        LabelStyle style = new LabelStyle();
        style.font = fontGenerator.generateFont(fontParameter);
        style.fontColor = Color.BLACK;
        progressLabel.setStyle(style);

        stage.addActor(progressLabel);
    }

    /**
     * TODO: Do this in the skin file...
     */
    private void createLevelCompleteUI() {
        if (Level.getInstance().getPlayer() != null){
            Level.getInstance().getPlayer().setDone();
        }

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        levelCompleteBackground = new Image(AssetManager.getInstance().getTextureRegion("default"));
        levelCompleteBackground.setPosition(0, 0);
        levelCompleteBackground.setSize(screenWidth, screenHeight);
        levelCompleteBackground.setColor(0.7f, 0.7f, 0.7f, 0.8f);
        stage.addActor(levelCompleteBackground);

        float nextButtonHeight = screenWidth / 4;
        FreeTypeFontParameter nextParameter = new FreeTypeFontParameter();
        nextParameter.size = (int)nextButtonHeight;
        nextParameter.borderWidth = nextParameter.size / 50;
        nextParameter.spaceX = (int)screenWidth / 100;
        TextButtonStyle nextButtonStyle = new TextButtonStyle();
        nextButtonStyle.font = fontGenerator.generateFont(nextParameter);
        nextButtonStyle.fontColor = Color.BLACK;
        nextButton = new TextButton("\nNEXT", skin);
        nextButton.setSize(screenWidth, nextButtonHeight);
        nextButton.setPosition(0, screenHeight / 2);
        nextButton.setStyle(nextButtonStyle);
        nextButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nextButton.getStyle().fontColor = Color.WHITE;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hideLevelComplete();
                nextButton.getStyle().fontColor = Color.BLACK;

                StatisticsListener statisticsListener = StatisticsListener.getInstance();
                long iterationId = statisticsListener.getIterationId();
                int levelId = (int)statisticsListener.getLevelId();
                Level.getInstance().load(iterationId, levelId);
            }
        });
        stage.addActor(nextButton);

        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = Gdx.graphics.getWidth() / 30;
        LabelStyle style = new LabelStyle();
        style.font = fontGenerator.generateFont(fontParameter);
        style.fontColor = Color.BLACK;
        levelSummaryLabel = new Label("", skin);
        levelSummaryLabel.setStyle(style);
        stage.addActor(levelSummaryLabel);
    }

    private void createMobileButtons() {
        createMobileMoveButton();
        createMobileJumpButton();
    }

    private void createMobileMoveButton() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        moveButton = new Button(skin);
        moveButton.setColor(1, 1, 1, BUTTON_ALPHA);
        moveButton.setSize(screenWidth / 3f, screenHeight / 5f);
        moveButton.setPosition(screenWidth / 32, screenHeight / 32f);
        moveButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Globals.getGameState() == GameState.WAIT_FOR_INPUT) {
                    Globals.setGameState(GameState.RUNNING);
                }

                if(Globals.getGameState() == GameState.RUNNING) {
                    movePointer = pointer;
                }
                return true;
            }
        });

        stage.addActor(moveButton);
    }

    private void createMobileJumpButton() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        jumpButton = new Button(skin);
        jumpButton.setColor(1, 1, 1, BUTTON_ALPHA);
        jumpButton.setSize(screenWidth / 6f, screenHeight / 5f);
        float buttonWidth = jumpButton.getWidth();
        jumpButton.setPosition(((31.0f / 32.0f) * screenWidth) - buttonWidth, screenHeight / 32f);
        jumpButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Globals.getGameState() == GameState.WAIT_FOR_INPUT) {
                    Globals.setGameState(GameState.RUNNING);
                }

                if(Globals.getGameState() == GameState.RUNNING) {
                    Level.getInstance().getPlayer().jump();
                }

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(Globals.getGameState() == GameState.RUNNING) {
                    Level.getInstance().getPlayer().stopJump();
                }
            }
        });

        stage.addActor(jumpButton);
    }

    private void checkMobileButtons() {
        if(movePointer == null || Globals.getGameState() != GameState.RUNNING) {
            return;
        }

        float moveCenterX = moveButton.getX() + (moveButton.getWidth() / 2);
        float x = Gdx.input.getX(movePointer);
        Player player = Level.getInstance().getPlayer();
        if(moveButton.isPressed()) {
            if(x < moveCenterX) {
                player.moveLeft();
            } else {
                player.moveRight();
            }
        } else {
            player.stopHorizontalMove();
        }
    }
}
