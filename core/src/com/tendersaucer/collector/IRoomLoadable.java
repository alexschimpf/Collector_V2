package com.tendersaucer.collector;

import com.tendersaucer.collector.entity.Entity;

import java.util.List;

/**
 * Interface for a loadable room
 *
 * Created by Alex on 4/9/2016.
 */
public interface IRoomLoadable {

    public List<Entity> getEntities();
}
