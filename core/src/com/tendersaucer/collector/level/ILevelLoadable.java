package com.tendersaucer.collector.level;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.background.ParallaxBackground;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.util.FixtureBodyDefinition;

import java.util.Map;

/**
 * Interface for a loadable level
 * <p/>
 * Created by Alex on 4/9/2016.
 */
public interface ILevelLoadable {

    /**
     * Returns the level's id
     *
     * @return level's id
     */
    String getId();

    /**
     * Returns the level's background
     *
     * @return background
     */
    ParallaxBackground getBackground();

    /**
     * Returns all initial entity definitions
     *
     * @return entity definitions
     */
    Array<EntityDefinition> getEntityDefinitions();

    /**
     * Returns all non-entity body definitions
     *
     * @return body definitions
     */
    Array<FixtureBodyDefinition> getFreeBodyDefinitions();

    /**
     * Returns map from [non-background/entity] IRender objects to their Canvas layer
     *
     * @return
     */
    Map<IRender, Integer> getCanvasMap();
}