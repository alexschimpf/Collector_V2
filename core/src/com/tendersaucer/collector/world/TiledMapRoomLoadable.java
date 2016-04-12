package com.tendersaucer.collector.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.Layers;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.util.TiledMapLayer;
import com.tendersaucer.collector.util.Utils;

/**
 * Loadable room from TiledMap
 *
 * Created by Alex on 4/8/2016.
 */
public final class TiledMapRoomLoadable implements IRoomLoadable {

    private final TiledMap tiledMap;
    private final Array<Entity> entities;
    private final Array<Body> nonEntityBodies;
    private final Array<IRender> renderLayers;

    public TiledMapRoomLoadable(String worldId, String roomId) {
        entities = new Array<Entity>();
        nonEntityBodies = new Array<Body>();
        renderLayers = new Array<IRender>(Layers.NUM_LAYERS);

        String filename = Utils.getRoomConfigURI(worldId, roomId);
        tiledMap = new TmxMapLoader().load(filename);
        for(MapLayer layer : tiledMap.getLayers()) {
            if((layer instanceof TiledMapTileLayer)) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer)layer;
                processLayer(tileLayer);

                TiledMapLayer layerWrapper = new TiledMapLayer(tileLayer);
                int layerPos = layerWrapper.getIntProperty("layer_pos");
                renderLayers.insert(layerPos, layerWrapper);
            }
        }
    }

    private void processLayer(TiledMapTileLayer layer) {
        // Extract entities and bodies
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    @Override
    public Array<Body> getNonEntityBodies() {
        return nonEntityBodies;
    }

    @Override
    public Array<IRender> getRenderLayers() {
        return renderLayers;
    }
}
