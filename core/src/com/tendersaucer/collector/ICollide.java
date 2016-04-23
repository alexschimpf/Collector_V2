package com.tendersaucer.collector;

import com.badlogic.gdx.physics.box2d.Contact;
import com.tendersaucer.collector.entity.Entity;

/**
 * Interface for colliding objects
 *
 * Created by Alex on 4/8/2016.
 */
public interface ICollide {

    public void onBeginContact(Contact contact, Entity entity);

    public void onEndContact(Contact contact, Entity entity);
}
