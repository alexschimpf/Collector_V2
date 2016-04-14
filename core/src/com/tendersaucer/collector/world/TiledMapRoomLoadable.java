package com.tendersaucer.collector.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.Layers;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.util.FileUtils;
import com.tendersaucer.collector.util.InvalidConfigException;
import com.tendersaucer.collector.util.TiledMapLayer;
import com.tendersaucer.collector.util.TiledUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Loadable room from TiledMap
 *
 * Map Properties:
 *   - background_filename
 *
 * Layers:
 *   - Each layer should have a "layer_pos" property (0-9)
 *   - One layer should be named "bodies", which holds all free bodies and body skeletons
 *   - The other layers will contain entity and other misc. objects
 *
 * Objects:
 *   - All objects (except free bodies) need a type property, to indicate the entity type
 *   - TextureMapObjects are reserved for entities
 *   - All other types of objects (e.g. shape objects) are for free bodies and body skeletons
 *   - Each entity object may be associated with a body skeleton (via its body_skeleton_id property)
 *     - Otherwise, the TextureMapObject, itself, will be used as the skeleton
 *
 * Created by Alex on 4/8/2016.
 */
public final class TiledMapRoomLoadable implements IRoomLoadable {

    private final String filename;
    private final TiledMap tiledMap;
    private final Array<Entity> entities;
    private final Array<Body> nonEntityBodies;
    private final Array<IRender> renderLayers;
    private final Map<String, MapObject> bodySkeletonMap;

    public TiledMapRoomLoadable(String worldId, String roomId) {
        entities = new Array<Entity>();
        nonEntityBodies = new Array<Body>();
        renderLayers = new Array<IRender>(Layers.NUM_LAYERS);
        bodySkeletonMap = new HashMap<String, MapObject>();

        filename = FileUtils.getRoomConfigURI(worldId, roomId);
        tiledMap = new TmxMapLoader().load(filename);

        processLayers();
        createBackground();
    }

    private void processLayers() {
        for(MapLayer layer : tiledMap.getLayers()) {
            if((layer instanceof TiledMapTileLayer)) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer)layer;
                TiledMapLayer layerWrapper = new TiledMapLayer(tileLayer);

                // Bodies must exist before entity objects.
                if (layerWrapper.getName().equals("bodies")) {
                    processLayer(layerWrapper);
                }

                int layerPos = layerWrapper.getIntProperty("layer_pos");
                if (layerPos < 0 || layerPos > Layers.NUM_LAYERS - 1) {
                    throw new InvalidConfigException(filename, "layer_pos", layerPos);
                }
                renderLayers.insert(layerPos, layerWrapper);
            }
        }

        for (IRender layer : renderLayers) {
            TiledMapLayer tiledMapLayer = (TiledMapLayer)layer;

            // We already processed the bodies layer.
            if (!tiledMapLayer.getName().equals("bodies")) {
                processLayer(tiledMapLayer);
            }
        }
    }

    private void processLayer(TiledMapLayer layer) {
        Array<MapObject> freeBodies = new Array<MapObject>();
        Array<TextureMapObject> entities = new Array<TextureMapObject>();

        for (MapObject object : layer.getObjects()) {
            if (object instanceof TextureMapObject) {
                entities.add((TextureMapObject)object);
            } else {
                String type = layer.getStringProperty(object, "type");
                if (isFreeBody(type)) {
                    freeBodies.add(object);
                } else if (isBodySkeleton(type)) {
                    bodySkeletonMap.put(object.getName(), object);
                } else {
                    throw new InvalidConfigException(filename, "type", type);
                }
            }
        }

        processFreeBodies(freeBodies);
        processEntities(entities);
    }

    private void processFreeBodies(Array<MapObject> bodies) {
        for(MapObject object : bodies) {
            if(object instanceof RectangleMapObject) {
                TiledUtils.createBodyFromRectangle(object);
            } else if(object instanceof PolylineMapObject) {
                TiledUtils.createBodyFromPolyline(object);
            } else if(object instanceof CircleMapObject) {
                TiledUtils.createBodyFromCircle(object);
            } else if(object instanceof EllipseMapObject) {
                TiledUtils.createBodyFromEllipse(object);
            } else if(object instanceof PolygonMapObject) {
                TiledUtils.createBodyFromPolygon(object);
            }
        }
    }

    private void processEntities(Array<TextureMapObject> entities) {
        for (TextureMapObject object : entities) {
            // TODO: Process entity
        }
    }

    private boolean isFreeBody(String type) {
        return type == null;
    }

    private boolean isBodySkeleton(String type) {
        return type != null && type.equals("body_skeleton");
    }

    private void createBackground() {
        // TODO: Get texture file(s) from some map property
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    @Override
    public Array<IRender> getRenderLayers() {
        return renderLayers;
    }
}
