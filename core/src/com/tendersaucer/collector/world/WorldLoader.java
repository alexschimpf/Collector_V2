package com.tendersaucer.collector.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.util.FileUtils;

import java.io.IOException;

/**
 * Loads world and its entry room
 *
 * Created by Alex on 4/10/2016.
 */
public final class WorldLoader {

    private final static Array<IWorldChangedListener> worldChangedListeners = new Array<IWorldChangedListener>();

    private WorldLoader() {
    }

    public static void addWorldChangedListener(IWorldChangedListener listener) {
        worldChangedListeners.add(listener);
    }

    public static void removeWorldChangedListener(IWorldChangedListener listener) {
        worldChangedListeners.removeValue(listener, true);
    }

    public static void load(String worldId) {
        Globals.getWorld().clearPhysicsWorld();

        try {
            XmlReader reader = new XmlReader();
            XmlReader.Element root = reader.parse(Gdx.files.internal(FileUtils.getWorldConfigURI(worldId)));
            loadEntryRoom(worldId, root);
        } catch (IOException e) {
            // TODO:
        }

        notifyWorldChangedListeners();
    }

    private static void loadEntryRoom(String worldId, XmlReader.Element root) {
        String roomId = root.getChildByName("entryRoomId").getText();
        IRoomLoadable roomLoadable = new TiledMapRoomLoadable(worldId, roomId);
        RoomLoader.load(roomLoadable);
    }

    private static void notifyWorldChangedListeners() {
        for (IWorldChangedListener listener : worldChangedListeners) {
            listener.onWorldChanged();
        }
    }
}