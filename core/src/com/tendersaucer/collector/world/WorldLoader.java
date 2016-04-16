package com.tendersaucer.collector.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.tendersaucer.collector.util.FileUtils;

import java.io.IOException;

/**
 * Loads world and its entry room
 *
 * Created by Alex on 4/10/2016.
 */
public final class WorldLoader {

    private static final WorldLoader instance = new WorldLoader();
    private static final String ENTRY_ROOM_ID_PROP = "entry_room_id";

    private final Array<IWorldChangedListener> worldChangedListeners;

    private WorldLoader() {
        worldChangedListeners = new Array<IWorldChangedListener>();
    }

    public static WorldLoader getInstance() {
        return instance;
    }

    public void addWorldChangedListener(IWorldChangedListener listener) {
        worldChangedListeners.add(listener);
    }

    public void removeWorldChangedListener(IWorldChangedListener listener) {
        worldChangedListeners.removeValue(listener, true);
    }

    public void load(String worldId) {
        World world = World.getInstance();
        world.clearPhysicsWorld();
        world.setId(worldId);

        try {
            XmlReader reader = new XmlReader();
            XmlReader.Element root = reader.parse(Gdx.files.internal(FileUtils.getWorldConfigURI(worldId)));
            loadEntryRoom(worldId, root);
        } catch (IOException e) {
            // TODO:
        }

        notifyWorldChangedListeners();
    }

    private void loadEntryRoom(String worldId, XmlReader.Element root) {
        String roomId = root.getChildByName(ENTRY_ROOM_ID_PROP).getText();
        World.getInstance().setEntryRoomId(roomId);
        RoomLoader.getInstance().load(worldId, roomId);
    }

    private void notifyWorldChangedListeners() {
        for (IWorldChangedListener listener : worldChangedListeners) {
            listener.onWorldChanged();
        }
    }
}