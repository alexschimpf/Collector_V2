package com.tendersaucer.collector.level;

import com.badlogic.gdx.physics.box2d.Contact;
import com.tendersaucer.collector.entity.Entity;

/**
 * Interface for colliding objects
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public interface ICollide {

    /**
     * Fired from CollisionListener when contact begins
     *
     * @param contact
     * @param entity
     */
    void onBeginContact(Contact contact, Entity entity);

    /**
     * Fired from CollisionListener when contact ends
     *
     * @param contact
     * @param entity
     */
    void onEndContact(Contact contact, Entity entity);
}
