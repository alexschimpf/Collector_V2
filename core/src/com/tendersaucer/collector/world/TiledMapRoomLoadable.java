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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.Camera;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.Layers;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.entity.EntityFactory;
import com.tendersaucer.collector.entity.TiledEntityDefinition;
import com.tendersaucer.collector.entity.TiledEntityPropertyValidator;
import com.tendersaucer.collector.util.FileUtils;
import com.tendersaucer.collector.util.InvalidConfigException;
import com.tendersaucer.collector.util.TiledMapLayer;
import com.tendersaucer.collector.util.TiledUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loadable room from TiledMap

 * Created by Alex on 4/8/2016.
 */
public final class TiledMapRoomLoadable implements IRoomLoadable {

    private final String filename;
    private final TiledMap tiledMap;
    private final Array<Entity> entities;
    private final Map<IRender, Integer> renderableLayerMap;
    private final Map<String, MapObject> bodySkeletonMap;

    public TiledMapRoomLoadable(String worldId, String roomId) {
        entities = new Array<Entity>();
        renderableLayerMap = new LinkedHashMap<IRender, Integer>();
        bodySkeletonMap = new HashMap<String, MapObject>();

        filename = FileUtils.getRoomConfigURI(worldId, roomId);
        tiledMap = new TmxMapLoader().load(filename);

        processLayers();
        createBackground();
    }

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    @Override
    public Map<IRender, Integer> getRenderableLayerMap() {
        return renderableLayerMap;
    }

    private void processLayers() {
        Array<TiledMapLayer> layersToProcess = new Array<TiledMapLayer>();
        for(MapLayer layer : tiledMap.getLayers()) {
            if((layer instanceof TiledMapTileLayer)) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer)layer;
                TiledMapLayer layerWrapper = new TiledMapLayer(tileLayer);

                // Bodies must exist before entity objects.
                if (layerWrapper.getName().equals(BODIES_LAYER)) {
                    processLayer(layerWrapper);
                } else {
                    int layerPos = layerWrapper.getIntProperty(LAYER_POS_PROP);
                    if (!isLayerPosValid(layerPos)) {
                        throw new InvalidConfigException(filename, LAYER_POS_PROP, layerPos);
                    }

                    layersToProcess.add(layerWrapper);
                    renderableLayerMap.put(layerWrapper, layerPos);
                }
            }
        }

        for (TiledMapLayer layer : layersToProcess) {
            processLayer(layer);
        }
    }

    private void processLayer(TiledMapLayer layer) {
        Array<MapObject> freeBodies = new Array<MapObject>();
        Array<TextureMapObject> entities = new Array<TextureMapObject>();

        for (MapObject object : layer.getObjects()) {
            if (object instanceof TextureMapObject) {
                entities.add((TextureMapObject)object);
            } else {
                String type = layer.getStringProperty(object, TYPE_PROP);
                if (isFreeBody(type)) {
                    freeBodies.add(object);
                } else if (isBodySkeleton(type)) {
                    bodySkeletonMap.put(object.getName(), object);
                } else {
                    throw new InvalidConfigException(filename, TYPE_PROP, type);
                }
            }
        }

        processFreeBodies(freeBodies);
        processEntities(layer, entities);
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

    private void processEntities(TiledMapLayer layer, Array<TextureMapObject> entities) {
        for (TextureMapObject object : entities) {
            if (layer.propertyExists(object, TYPE_PROP)) {
                throw new InvalidConfigException(filename, TYPE_PROP, "null");
            }

            String type = layer.getStringProperty(object, TYPE_PROP);

            TiledEntityPropertyValidator.getInstance().validateAndProcess(type, object.getProperties());

            // Determine body skeleton.
            MapObject bodySkeleton = object;
            if (layer.propertyExists(object, BODY_SKELETON_ID_PROP)) {
                String bodySkeletonId = layer.getStringProperty(BODY_SKELETON_ID_PROP);
                if (!bodySkeletonMap.containsKey(bodySkeletonId)) {
                  throw new InvalidConfigException(filename, BODY_SKELETON_ID_PROP, bodySkeletonId);
                }

                bodySkeleton = bodySkeletonMap.get(bodySkeletonId);
            } else if (layer.propertyExists(object, BODY_WIDTH_PROP) &&
                    layer.propertyExists(object, BODY_HEIGHT_PROP)) {
                float bodyWidth = layer.getFloatProperty(object, BODY_WIDTH_PROP);
                float bodyHeight = layer.getFloatProperty(object, BODY_HEIGHT_PROP);

                bodySkeleton = new RectangleMapObject();
                ((RectangleMapObject)bodySkeleton).getRectangle().setSize(bodyWidth, bodyHeight);
            }

            BodyDef bodyDef = getBodyDef(layer, object);
            EntityDefinition entityDef = new TiledEntityDefinition(object.getName(), bodyDef,
                    bodySkeleton, object.getProperties());
            Entity entity = EntityFactory.buildEntity(type, entityDef);
            this.entities.add(entity);

            int layerPos = getEntityLayerPos(layer, object);
            renderableLayerMap.put(entity, layerPos);
        }
    }

    private void createBackground() {
        // TODO: Get texture file(s) from some map property
    }

    private int getEntityLayerPos(TiledMapLayer layer, TextureMapObject object) {
        if (layer.propertyExists(object, LAYER_POS_PROP)) {
            return layer.getIntProperty(object, LAYER_POS_PROP);
        } else if (layer.propertyExists(LAYER_POS_PROP)) {
            return layer.getIntProperty(LAYER_POS_PROP);
        } else {
            throw new InvalidConfigException(filename, LAYER_POS_PROP, "null");
        }
    }

    private BodyDef getBodyDef(TiledMapLayer layer, MapObject object) {
        if(!layer.propertyExists(BODY_TYPE_PROP)) {
            throw new InvalidConfigException(filename, BODY_TYPE_PROP, "null");
        }

        BodyDef.BodyType bodyType;
        String bodyTypeStr = layer.getStringProperty(BODY_TYPE_PROP);
        if(bodyTypeStr.equals(STATIC_BODY_TYPE)) {
            bodyType = BodyDef.BodyType.StaticBody;
        } else if(bodyTypeStr.equals(KINEMATIC_BODY_TYPE)) {
            bodyType = BodyDef.BodyType.KinematicBody;
        } else if(bodyTypeStr.equals(DYNAMIC_BODY_TYPE)) {
            bodyType = BodyDef.BodyType.DynamicBody;
        } else {
            throw new InvalidConfigException(filename, BODY_TYPE_PROP, bodyTypeStr);
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(getObjectPos(layer, object));

        return bodyDef;
    }

    private Vector2 getObjectPos(TiledMapLayer layer, MapObject object) {
        float unitScale = Camera.getInstance().getTileMapScale();
        float width = layer.getFloatProperty(object, WIDTH_PROP) * unitScale;
        float height = layer.getFloatProperty(object, HEIGHT_PROP) * unitScale;
        float x = (layer.getFloatProperty(object, X_PROP) * unitScale) + (width / 2);
        float y = (layer.getFloatProperty(object, Y_PROP) * unitScale) - (height / 2);

        return new Vector2(x, y);
    }

    private boolean isLayerPosValid(int layerPos) {
        return layerPos > -1 && layerPos < Layers.NUM_LAYERS;
    }

    private boolean isFreeBody(String type) {
        return type == null;
    }

    private boolean isBodySkeleton(String type) {
        return type != null && type.equals(BODY_SKELETON_TYPE);
    }
}
