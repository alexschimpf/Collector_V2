package com.tendersaucer.collector.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Contact;
import com.tendersaucer.collector.GameState;
import com.tendersaucer.collector.Globals;

/**
 * Created by Alex on 5/31/2016.
 */
public class NextLevelToken extends RenderedEntity {

    private boolean obtained;

    public NextLevelToken(EntityDefinition def) {
        super(def);

        obtained = false;
        body.setAngularVelocity(1.5f);
        sprite.setColor(Color.RED);

        body.getFixtureList().get(0).setSensor(true);
    }

    @Override
    public void onBeginContact(Contact contact, Entity entity) {
        if (!obtained && Entity.isPlayer(entity)) {
            obtained = true;

            Gdx.app.log("NextLevelToken", "Next level token obtained...");
            Globals.setGameState(GameState.LEVEL_COMPLETE);
        }
    }
}