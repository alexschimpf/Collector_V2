package com.tendersaucer.collector.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.AssetManager;

/**
 * Created by Alex on 5/12/2016.
 */
public class JSONWorldLoadable implements IWorldLoadable {

    protected static final String WORLDS_DIR = "worlds";
    protected static final String ENTRY_ROOM_ID_PROP = "entry_room_id";
    private static final String WORLD_CONFIG_NAME = "world.json";

    protected String id;
    protected String entryRoomId;

    public JSONWorldLoadable(String id) {
        this.id = id;

        String configURI = AssetManager.getFilePath(WORLDS_DIR, id, WORLD_CONFIG_NAME);
        processJSON(configURI);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEntryRoomId() {
        return entryRoomId;
    }

    protected void processJSON(String configURI) {
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(Gdx.files.internal(configURI));
        entryRoomId = root.getString(ENTRY_ROOM_ID_PROP);
    }
}
