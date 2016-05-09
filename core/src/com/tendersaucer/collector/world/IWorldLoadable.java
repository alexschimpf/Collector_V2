package com.tendersaucer.collector.world;

/**
 * Interface for a loadable world
 *
 * Created by Alex on 4/23/2016.
 */
public interface IWorldLoadable {

    /**
     * Returns the world's id. This id should be unique
     * @return world's id
     */
    String getId();

    /**
     * Returns the world's entry room's id
     * @return entry room's id
     */
    String getEntryRoomId();
}
