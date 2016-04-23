package com.tendersaucer.collector;

import com.tendersaucer.collector.entity.PlayerInteractionType;

/**
 * Interface for interactive objects
 *
 * Created by Alex on 4/8/2016.
 */
public interface IInteractive {

    /**
     * Fired when the player interacts with this object.
     * @param interactionType
     */
    void onInteraction(PlayerInteractionType interactionType);
}
