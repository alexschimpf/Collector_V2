package com.tendersaucer.collector.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.particle.ParticleEffectManager;

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
        return false;
    }

    @Override
    public boolean keyUp(InputEvent event, int keyCode) {
        return false;
    }

    @Override
    public boolean keyDown(InputEvent event, int keyCode) {
        switch (keyCode) {
            case Keys.LEFT:
                Globals.getPlayer().moveLeft();
                break;
            case Keys.RIGHT:
                Globals.getPlayer().moveRight();
                break;
            case Keys.SPACE:
                Globals.getPlayer().jump();
                break;
            case Keys.ESCAPE:
                Gdx.app.exit();
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
