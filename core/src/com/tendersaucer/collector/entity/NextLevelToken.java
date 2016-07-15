package com.tendersaucer.collector.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Contact;
import com.tendersaucer.collector.level.Level;

/**
 * Created by Alex on 5/31/2016.
 */
public class NextLevelToken extends RenderedEntity {

    public NextLevelToken(EntityDefinition def) {
        super(def);

        body.setAngularVelocity(0.1f);
        sprite.setColor(Color.RED);
    }

    @Override
    public void onBeginContact(Contact contact, Entity entity) {
        if (Entity.isPlayer(entity)) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Level.getInstance().loadNext();
                }
            });
        }
    }
}