package com.tendersaucer.collector.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.tendersaucer.collector.util.Utils;

import java.io.IOException;

/**
 * Loads world and its entry room
 *
 * Created by Alex on 4/10/2016.
 */
public final class WorldLoader {

    private WorldLoader() {
    }

    public static void load(String worldId) {
        try {
            XmlReader reader = new XmlReader();
            XmlReader.Element root = reader.parse(Gdx.files.internal(Utils.getWorldConfigURI("0")));
            loadEntryRoom(worldId, root);
        } catch (IOException e) {
            // TODO:
        }
    }

    private static void loadEntryRoom(String worldId, XmlReader.Element root) {
        String roomId = root.getChildByName("entryRoomId").getText();
        IRoomLoadable roomLoadable = new TiledMapRoomLoadable(worldId, roomId);
        RoomLoader.load(roomLoadable);
    }
}