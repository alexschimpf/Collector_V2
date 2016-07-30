package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.tendersaucer.collector.GameState;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.MainCamera;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.level.Level;
import com.tendersaucer.collector.statistics.StatisticsDAO;

/**
 * Game input listener
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class InputListener extends com.badlogic.gdx.scenes.scene2d.InputListener implements IUpdate {

    public InputListener() {
    }

    @Override
    public boolean update() {
        if (Globals.getGameState() != GameState.RUNNING) {
            return false;
        }

        MainCamera camera = MainCamera.getInstance();
        Player player = Level.getInstance().getPlayer();
        if (player != null) {
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                if (Globals.CUSTOM_CAMERA_MODE && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
                    camera.move(camera.getTileSize() / 2, 0);
                    camera.setPlayerFocus(false);
                } else {
                    player.moveRight();
                }
            } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                if (Globals.CUSTOM_CAMERA_MODE && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
                    camera.move(-camera.getTileSize() / 2, 0);
                    camera.setPlayerFocus(false);
                } else {
                    player.moveLeft();
                }
            } else if (Globals.CUSTOM_CAMERA_MODE && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
                if (Gdx.input.isKeyPressed(Keys.UP)) {
                    camera.move(0, -camera.getTileSize() / 2);
                    camera.setPlayerFocus(false);
                } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                    camera.move(0, camera.getTileSize() / 2);
                }
            } else if (!Globals.isAndroid()) {
                player.stopHorizontalMove();
            }
        }

        return false;
    }

    @Override
    public boolean keyDown(InputEvent event, int keyCode) {
        if (Globals.getGameState() == GameState.RUNNING) {
            if (keyCode == Keys.A) {
                Player player = Level.getInstance().getPlayer();
                if (player != null) {
                    player.jump();
                }
            }
        }

        switch (keyCode) {
            case Keys.ESCAPE:
                Gdx.app.exit();
                break;
            case Keys.Z:
                MainCamera.getInstance().getRawCamera().zoom += 0.1f;
                break;
            case Keys.X:
                MainCamera.getInstance().getRawCamera().zoom -= 0.1f;
                break;
            case Keys.D:
                Globals.DEBUG_PHYSICS = !Globals.DEBUG_PHYSICS;
                break;
            case Keys.C:
                StatisticsDAO.getInstance().clear();
                break;
            case Keys.M:
                Globals.ENABLE_MUSIC = !Globals.ENABLE_MUSIC;
                break;
            case Keys.P:
                Globals.PRINT_DEBUG_INFO = !Globals.PRINT_DEBUG_INFO;
                break;
            case Keys.E:
                Globals.CUSTOM_CAMERA_MODE = !Globals.CUSTOM_CAMERA_MODE;
                MainCamera.getInstance().setPlayerFocus(true);
                break;
            case Keys.SPACE:
                break;
            default:
                if (Globals.getGameState() != GameState.RUNNING) {
                    Globals.setGameState(GameState.RUNNING);
                }

                return false;
        }

        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keyCode) {
        if (Globals.getGameState() != GameState.RUNNING ) {
            return false;
        }

        if (keyCode == Keys.A) {
            Player player = Level.getInstance().getPlayer();
            if (player != null) {
                player.stopJump();
            }
        }

        return true;
    }
}
