package com.tendersaucer.collector.world.room;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.util.FixtureBodyDefinition;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.background.ParallaxBackground;
import com.tendersaucer.collector.entity.EntityDefinition;

import java.util.Map;

/**
 * Interface for a loadable room
 *
 * Created by Alex on 4/9/2016.
 */
public interface IRoomLoadable {

    /**
     * Returns the room's id. This id should be unique within a world
     * @return room's id
     */
    String getId();

    /**
     * Returns the room's background
     * @return background
     */
    ParallaxBackground getBackground();

    /**
     * Returns all initial entity definitions
     * @return entity definitions
     */
    Array<EntityDefinition> getEntityDefinitions();

    /**
     * Returns all non-entity body definitions
     * @return body definitions
     */
    Array<FixtureBodyDefinition> getFreeBodyDefinitions();

    /**
     * Returns map from [non-background/entity] IRender objects to their Canvas layer
     * @return
     */
    Map<IRender, Integer> getCanvasMap();
}