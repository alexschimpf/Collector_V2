package com.tendersaucer.collector.world;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.entity.Entity;

/**
 * Interface for a loadable room
 *
 * Created by Alex on 4/9/2016.
 */
public interface IRoomLoadable {

    public Array<Entity> getEntities();

    public Array<IRender> getRenderLayers();
}
