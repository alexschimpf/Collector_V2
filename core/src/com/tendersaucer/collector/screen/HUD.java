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
    private Button nextButton;
    private Button replayButton;
    private Button moveButton;
    private Button jumpButton;
    private Button interactButton;
    private Integer movePointer;
    private Skin skin;

    private HUD() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        inputListener = new InputListener();
        stage.addListener(inputListener);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        createLevelCompleteButtons();
        hideLevelCompleteButtons();

        if (Globals.isDesktop()) {
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

    /**
     * TODO: Do this in skin file...
     */
    private void createMobileButtons() {

    }

    /**
     * TODO: Do this in the skin file...
     */
    private void createLevelCompleteButtons() {
        if (Level.getInstance().getPlayer() != null){
            Level.getInstance().getPlayer().setDone();
        }

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

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
            public void clicked(InputEvent event, float x, float y) {
                hideLevelCompleteButtons();
                Level.getInstance().loadNext();
            }
        });

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
            public void clicked(InputEvent event, float x, float y) {
                hideLevelCompleteButtons();
                Level.getInstance().replay();
            }
        });

        stage.addActor(replayButton);
        stage.addActor(nextButton);
    }	
    
    private void checkPressedButtons() {
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

    private void buildMobileUI() {
        createMoveButton();
        createJumpButton();
        createInteractButton();
    }

    private void createMoveButton() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        moveButton = new Button(skin);
        moveButton.setColor(1, 1, 1, BUTTON_ALPHA);
        moveButton.setSize(screenWidth / 2.75f, screenHeight / 5f);
        moveButton.setPosition(0, screenHeight / 32f);

        moveButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(Globals.getGameState() == GameState.RUNNING) {
                    movePointer = pointer;
                }

                return true;
            }
        });

        stage.addActor(moveButton);
    }

    private void createJumpButton() {
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

    private void createInteractButton() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        interactButton = new Button(skin);
        interactButton.setColor(1, 1, 1, BUTTON_ALPHA);
        interactButton.setSize(screenWidth / 7f, screenHeight / 5f);
        float buttonWidth = interactButton.getWidth();

        interactButton.setPosition(screenWidth - (buttonWidth * 1.1f), screenHeight / 32f);
        interactButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(interactButton);
    }
    
    
}
