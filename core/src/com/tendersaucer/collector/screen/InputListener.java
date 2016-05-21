package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.MainCamera;
import com.tendersaucer.collector.entity.Player;
import com.tendersaucer.collector.particle.ParticleEffectManager;
import com.tendersaucer.collector.world.room.Room;

/**
 * Game input listener
 *
 * Created by Alex on 4/8/2016.
 */
public final class InputListener extends com.badlogic.gdx.scenes.scene2d.InputListener implements IUpdate {

    public InputListener() {
    }

    @Override
    public boolean update() {
        Player player = Room.getInstance().getPlayer();
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
            player.moveRight();
        } else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
            player.moveLeft();
        } else if(!Globals.isAndroid()){
            player.stopMove();
        }

        return false;
    }

    @Override
    public boolean keyUp(InputEvent event, int keyCode) {
        switch(keyCode) {
            case Keys.SPACE:
                Room.getInstance().getPlayer().stopJump();
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public boolean keyDown(InputEvent event, int keyCode) {
        switch (keyCode) {
            case Keys.SPACE:
                Room.getInstance().getPlayer().jump();
                break;
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
                ParticleEffectManager.getInstance().clearLiveEffects();
                break;
            case Keys.M:
                Globals.ENABLE_MUSIC = !Globals.ENABLE_MUSIC;
                break;
            case Keys.P:
                Globals.PRINT_DEBUG_INFO = !Globals.PRINT_DEBUG_INFO;
                break;
            default:
                return false;
        }

        return true;
    }
}
