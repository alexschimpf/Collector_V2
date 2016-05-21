package com.tendersaucer.collector.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.AssetManager;

/**
 * Created by Alex on 5/12/2016.
 */
public class JSONWorldLoadable implements IWorldLoadable {

    protected String id;
    protected String entryRoomId;

    public JSONWorldLoadable(String id) {
        this.id = id;

        String configURI = AssetManager.getFilePath("worlds", id, "world.json");
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
        entryRoomId = root.getString("entry_room_id");
    }
}
