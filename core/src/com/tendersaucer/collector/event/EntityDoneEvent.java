package com.tendersaucer.collector.event;

import com.tendersaucer.collector.entity.Entity;

/**
 * Created by Alex on 5/5/2016.
 */
public final class EntityDoneEvent extends Event<IEntityDoneListener> {

    private Entity entity;

    public EntityDoneEvent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void notify(IEntityDoneListener listener) {
        listener.onEntityDone(entity);
    }
}
