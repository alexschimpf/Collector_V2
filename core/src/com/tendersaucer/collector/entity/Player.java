package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * User-controlled player
 *
 * Created by Alex on 4/8/2016.
 */
public final class Player extends Entity {

    public Player(TiledEntityDefinition def) {
        super(def);
    }

    @Override
    public String getType() {
        return "player";
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    protected void tick() {
    }

    @Override
    protected void onDone() {
    }

    @Override
    public void onBeginContact(Contact contact, Entity entity) {

    }

    @Override
    public void onEndContact(Contact contact, Entity entity) {

    }
}
