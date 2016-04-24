package com.tendersaucer.collector.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.tendersaucer.collector.util.FileUtils;

import java.io.IOException;

/**
 * Loadable world from XML
 *
 * Created by Alex on 4/23/2016.
 */
public class XMLWorldLoadable implements IWorldLoadable {

    protected String id;
    protected String entryRoomId;

    public XMLWorldLoadable(String id) {
        this.id = id;

        String configURI = FileUtils.getWorldConfigURI(id);
        processXML(configURI);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEntryRoomId() {
        return entryRoomId;
    }

    protected void processXML(String configURI) {
        XmlReader reader = new XmlReader();
        try {
            XmlReader.Element root = reader.parse(Gdx.files.internal(configURI));
            entryRoomId = root.getChildByName(ENTRY_ROOM_ID_PROP).getText();
        } catch (IOException e) {
            // TODO
        }
    }
}
