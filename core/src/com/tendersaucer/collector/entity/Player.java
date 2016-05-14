package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 * User-controlled player
 *
 * Created by Alex on 4/8/2016.
 */
public final class Player extends VisibleEntity {

    public static final String TYPE = "player";
    public static final short COLLISION_MASK = 0x0002;

    public Player(EntityDefinition def) {
        super(def);
    }

    @Override
    public String getType() {
        return "player";
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);

        System.out.println("Rendering player...");
        System.out.println("Position: " + getLeft() + ", " + getTop());
        System.out.println("Size: " + getWidth() + " x " + getHeight());
    }

    @Override
    protected void tick() {
        super.tick();

        System.out.println("Updating player....");
    }

    @Override
    public void onBeginContact(Contact contact, Entity entity) {

    }

    @Override
    public void onEndContact(Contact contact, Entity entity) {

    }
}
