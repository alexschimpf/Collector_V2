package com.tendersaucer.collector.level;

import com.badlogic.gdx.math.Vector2;
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
     * Returns the level's id. This increments by 1 for each level reached.
     * In other words, the next level = curr + 1.
     * @return level's id
     */
    int getId();

    /**
     * Returns the level's respawn position for the player.
     * @return respawn position
     */
    Vector2 getRespawnPosition();

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