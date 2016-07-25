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
    private TextButton nextButton;
    private TextButton replayButton;
    private Button moveButton;
    private Button jumpButton;
    private Integer movePointer;
    private Skin skin;

    private HUD() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        inputListener = new InputListener();
        stage.addListener(inputListener);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
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

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        return false;
    }

    @Override
    public void onGameStateChange(GameState oldEvent, GameState newEvent) {
        if (newEvent == GameState.LEVEL_COMPLETE) {
            Gdx.app.log("HUD", "Showing level completion dialog...");
            showLevelComplete();
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
        replayButton.setDisabled(true);
        nextButton.setVisible(false);
        replayButton.setVisible(false);
    }

    private void showLevelComplete() {
        levelCompleteBackground.setVisible(true);
        nextButton.setDisabled(false);
        replayButton.setDisabled(false);
        nextButton.setVisible(true);
        replayButton.setVisible(true);
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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

        float nextButtonHeight = screenHeight * 0.6f;
        FreeTypeFontParameter nextParameter = new FreeTypeFontParameter();
        nextParameter.size = (int)(screenWidth / 4);
        nextParameter.borderWidth = nextParameter.size / 50;
        nextParameter.spaceX = (int)screenWidth / 100;
        nextParameter.spaceY = -nextParameter.size + (nextParameter.size / 2);
        TextButtonStyle nextButtonStyle = new TextButtonStyle();
        nextButtonStyle.font = generator.generateFont(nextParameter);
        nextButtonStyle.fontColor = Color.RED;
        nextButton = new TextButton("\nNEXT", skin);
        nextButton.setSize(screenWidth, nextButtonHeight);
        nextButton.setPosition(0, screenHeight - nextButtonHeight);
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
                Level.getInstance().loadNext();
                nextButton.getStyle().fontColor = Color.RED;
            }
        });
        stage.addActor(nextButton);

        float replayButtonHeight = screenHeight * 0.4f;
        FreeTypeFontParameter replayParameter = new FreeTypeFontParameter();
        replayParameter.size = (int)(screenWidth / 20);
        replayParameter.spaceX = (int)screenWidth / 100;
        TextButtonStyle replayButtonStyle = new TextButtonStyle();
        replayButtonStyle.font = generator.generateFont(replayParameter);
        replayButtonStyle.fontColor = Color.BLACK;
        replayButton = new TextButton("REPLAY", skin);
        replayButton.setSize(screenWidth, replayButtonHeight);
        replayButton.setPosition(0, 0);
        replayButton.setStyle(replayButtonStyle);
        replayButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                replayButton.getStyle().fontColor = Color.WHITE;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                replayButton.getStyle().fontColor = Color.BLACK;
                hideLevelComplete();
                Level.getInstance().replay();
            }
        });
        stage.addActor(replayButton);
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
        moveButton.setSize(screenWidth / 2.75f, screenHeight / 5f);
        moveButton.setPosition(0, screenHeight / 32f);
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
        jumpButton.setSize(screenWidth / 7f, screenHeight / 5f);
        float buttonWidth = jumpButton.getWidth();

        jumpButton.setPosition(screenWidth - (buttonWidth * 2.2f), screenHeight / 32f);
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
