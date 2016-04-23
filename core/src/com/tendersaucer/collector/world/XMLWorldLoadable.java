package com.tendersaucer.collector.world;

/**
 * Created by Alex on 4/23/2016.
 */
public class XMLWorldLoadable implements IWorldLoadable {

    protected String id;

    public XMLWorldLoadable(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEntryRoomId() {
        return null;
    }
}
