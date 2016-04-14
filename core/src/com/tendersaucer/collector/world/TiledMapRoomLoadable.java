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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.Camera;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.Layers;
import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.entity.EntityFactory;
import com.tendersaucer.collector.entity.TiledEntityDefinition;
import com.tendersaucer.collector.util.FileUtils;
import com.tendersaucer.collector.util.InvalidConfigException;
import com.tendersaucer.collector.util.TiledMapLayer;
import com.tendersaucer.collector.util.TiledUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Loadable room from TiledMap

 * Created by Alex on 4/8/2016.
 */
public final class TiledMapRoomLoadable implements IRoomLoadable {

    private static final String TYPE_PROP = "type";
    private static final String LAYER_POS_PROP = "layer_pos";
    private static final String X_PROP = "x";
    private static final String Y_PROP = "y";
    private static final String WIDTH_PROP = "width";
    private static final String HEIGHT_PROP = "height";
    private static final String BODY_WIDTH_PROP = "body_width";
    private static final String BODY_TYPE_PROP = "body_type";
    private static final String KINEMATIC_BODY_TYPE = "kinematic";
    private static final String DYNAMIC_BODY_TYPE = "dynamic";
    private static final String STATIC_BODY_TYPE = "static";
    private static final String BODY_HEIGHT_PROP = "body_height";
    private static final String BODY_SKELETON_ID_PROP = "body_skeleton_id";
    private static final String BODY_SKELETON_TYPE = "body_skeleton";
    private static final String PLAYER_TYPE = "player";
    private static final String BODIES_LAYER = "bodies";

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

    @Override
    public Array<Entity> getEntities() {
        return entities;
    }

    @Override
    public Array<IRender> getRenderLayers() {
        return renderLayers;
    }

    private void processLayers() {
        for(MapLayer layer : tiledMap.getLayers()) {
            if((layer instanceof TiledMapTileLayer)) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer)layer;
                TiledMapLayer layerWrapper = new TiledMapLayer(tileLayer);

                // Bodies must exist before entity objects.
                if (layerWrapper.getName().equals(BODIES_LAYER)) {
                    processLayer(layerWrapper);
                }

                int layerPos = layerWrapper.getIntProperty(LAYER_POS_PROP);
                if (!isLayerPosValid(layerPos)) {
                    throw new InvalidConfigException(filename, LAYER_POS_PROP, layerPos);
                }
                renderLayers.insert(layerPos, layerWrapper);
            }
        }

        for (IRender layer : renderLayers) {
            TiledMapLayer tiledMapLayer = (TiledMapLayer)layer;

            // We already processed the bodies layer.
            if (!tiledMapLayer.getName().equals(BODIES_LAYER)) {
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

            TiledEntityPropertyValidator.getInstance().validate(type, object.getProperties());

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
            this.entities.add(EntityFactory.buildEntity(type, entityDef));
        }
    }

    private void createBackground() {
        // TODO: Get texture file(s) from some map property
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
        bodyDef.position.set(getObjectPosition(layer, object));

        return bodyDef;
    }

    private Vector2 getObjectPosition(TiledMapLayer layer, MapObject object) {
        float unitScale = Camera.getInstance().getTileMapScale();
        float width = layer.getFloatProperty(object, WIDTH_PROP) * unitScale;
        float height = layer.getFloatProperty(object, HEIGHT_PROP) * unitScale;
        float x = (layer.getFloatProperty(object, X_PROP) * unitScale) + (width / 2);
        float y = (layer.getFloatProperty(object, Y_PROP) * unitScale) - (height / 2);

        return new Vector2(x, y);
    }

    private boolean isLayerPosValid(int layerPos) {
        return layerPos >= 0 && layerPos <= Layers.NUM_LAYERS - 1 &&
                layerPos != Layers.PARTICLE_LAYER && layerPos != Layers.WORLD_LAYER;
    }

    private boolean isFreeBody(String type) {
        return type == null;
    }

    private boolean isBodySkeleton(String type) {
        return type != null && type.equals(BODY_SKELETON_TYPE);
    }
}
