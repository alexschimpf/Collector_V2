package com.tendersaucer.collector.util;

import com.tendersaucer.collector.entity.Entity;

/**
 * Object metadata to be attached to a body
 *
 * Created by Alex on 4/8/2016.
 */
public final class BodyData {

    public final Entity entity;

    public BodyData(Entity entity) {
        this.entity = entity;
    }
}
