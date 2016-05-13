package com.tendersaucer.collector.world.room;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.MainCamera;
import com.tendersaucer.collector.background.ParallaxBackground;
import com.tendersaucer.collector.background.TextureParallaxLayer;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.entity.TiledEntityDefinition;
import com.tendersaucer.collector.entity.TiledEntityPropertyValidator;
import com.tendersaucer.collector.screen.Canvas;
import com.tendersaucer.collector.screen.Driver;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.util.FileUtils;
import com.tendersaucer.collector.util.FixtureBodyDefinition;
import com.tendersaucer.collector.util.InvalidConfigException;
import com.tendersaucer.collector.util.MapLayerWrapper;
import com.tendersaucer.collector.util.TiledUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loadable room from TiledMap

 * Created by Alex on 4/8/2016.
 */
public final class TiledMapRoomLoadable implements IRoomLoadable {

    private static final String BACKGROUND_PROP = "background";
    private static final String TYPE_PROP = "type";
    private static final String LAYER_POS_PROP = "layer_pos";
    private static final String X_PROP = "x";
    private static final String Y_PROP = "y";
    private static final String WIDTH_PROP = "width";
    private static final String HEIGHT_PROP = "height";
    private static final String BODY_WIDTH_PROP = "body_width";
    private static final String BODY_HEIGHT_PROP = "body_height";
    private static final String BODY_SKELETON_ID_PROP = "body_skeleton_id";
    private static final String BODY_SKELETON_TYPE = "body_skeleton";
    private static final String PLAYER_TYPE = "player";
    private static final String BODIES_LAYER = "bodies";

    private final String id;
    private final String filename;
    private final TiledMap tiledMap;
    private final ParallaxBackground background;
    private final Array<FixtureBodyDefinition> freeBodyDefinitions;
    private final Array<EntityDefinition> entityDefinitions;
    private final Map<IRender, Integer> canvasMap;
    private final Map<String, MapObject> bodySkeletonMap;

    public TiledMapRoomLoadable(String worldId, String roomId) {
        this.id = roomId;

        freeBodyDefinitions = new Array<FixtureBodyDefinition>();
        entityDefinitions = new Array<EntityDefinition>();
        canvasMap = new LinkedHashMap<IRender, Integer>();
        bodySkeletonMap = new HashMap<String, MapObject>();

        filename = FileUtils.getRoomConfigURI(worldId, roomId);
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.flipY = false;
        tiledMap = new TmxMapLoader().load(filename, params);

        processLayers();

        background = new ParallaxBackground();
        setBackground();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ParallaxBackground getBackground() {
        return background;
    }

    @Override
    public Array<EntityDefinition> getEntityDefinitions() {
        return entityDefinitions;
    }

    @Override
    public Array<FixtureBodyDefinition> getFreeBodyDefinitions() {
        return freeBodyDefinitions;
    }

    @Override
    public Map<IRender, Integer> getCanvasMap() {
        return canvasMap;
    }

    private void processLayers() {
        final OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(tiledMap,
                MainCamera.getInstance().getTileMapScale(), Driver.getInstance().getSpriteBatch());
        renderer.setView(MainCamera.getInstance().getRawCamera());

        Array<MapLayerWrapper> layersToProcess = new Array<MapLayerWrapper>();
        for(MapLayer layer : tiledMap.getLayers()) {
            MapLayerWrapper layerWrapper = new MapLayerWrapper(renderer, layer);

            // Bodies must exist before entity objects.
            if (layerWrapper.getName().equals(BODIES_LAYER)) {
                processLayer(layerWrapper);
            } else {
                int layerPos = TiledUtils.getIntProperty(layerWrapper, LAYER_POS_PROP);
                if (!isLayerPosValid(layerPos)) {
                    throw new InvalidConfigException(filename, LAYER_POS_PROP, layerPos);
                }

                layersToProcess.add(layerWrapper);
                canvasMap.put(layerWrapper, layerPos);
            }
        }

        for (MapLayerWrapper layer : layersToProcess) {
            processLayer(layer);
        }
    }

    private void processLayer(MapLayerWrapper layer) {
        Array<MapObject> freeBodies = new Array<MapObject>();
        Array<TextureMapObject> entities = new Array<TextureMapObject>();

        for (MapObject object : layer.getObjects()) {
            if (object instanceof TextureMapObject) {
                entities.add((TextureMapObject)object);
            } else {
                if (!TiledUtils.propertyExists(object, TYPE_PROP)) {
                    freeBodies.add(object);
                } else {
                    String type = TiledUtils.getStringProperty(object, TYPE_PROP);
                    if (isBodySkeleton(type)) {
                        bodySkeletonMap.put(object.getName(), object);
                    } else {
                        throw new InvalidConfigException(filename, TYPE_PROP, type);
                    }
                }
            }
        }

        processFreeBodies(freeBodies);
        processEntities(layer, entities);
    }

    private void processFreeBodies(Array<MapObject> bodies) {
        for (MapObject object : bodies) {
            FixtureBodyDefinition fixtureBodyDefinition;
            if (object instanceof RectangleMapObject) {
                fixtureBodyDefinition = TiledUtils.createRectangleFixtureBodyDef((RectangleMapObject)object);
            } else if (object instanceof CircleMapObject) {
                fixtureBodyDefinition = TiledUtils.createCircleFixtureBodyDef((CircleMapObject)object);
            } else if (object instanceof EllipseMapObject) {
                fixtureBodyDefinition = TiledUtils.createEllipseFixtureBodyDef((EllipseMapObject)object);
            } else if (object instanceof PolylineMapObject || object instanceof PolygonMapObject) {
                fixtureBodyDefinition = TiledUtils.createPolyFixtureBodyDef(object);
            } else {
                throw new InvalidConfigException(filename, "Unknown MapObject type");
            }

            freeBodyDefinitions.add(fixtureBodyDefinition);
        }
    }

    private void processEntities(MapLayerWrapper layer, Array<TextureMapObject> entities) {
        for (TextureMapObject object : entities) {
            if (TiledUtils.propertyExists(object,TYPE_PROP)) {
                throw new InvalidConfigException(filename, TYPE_PROP, "null");
            }

            String type = TiledUtils.getStringProperty(object, TYPE_PROP);

            TiledEntityPropertyValidator.validateAndProcess(type, object.getProperties());

            // Determine body skeleton.
            MapObject bodySkeleton = object;
            if (TiledUtils.propertyExists(object, BODY_SKELETON_ID_PROP)) {
                String bodySkeletonId = TiledUtils.getStringProperty(object, BODY_SKELETON_ID_PROP);
                if (!bodySkeletonMap.containsKey(bodySkeletonId)) {
                  throw new InvalidConfigException(filename, BODY_SKELETON_ID_PROP, bodySkeletonId);
                }

                bodySkeleton = bodySkeletonMap.get(bodySkeletonId);
            } else if (TiledUtils.propertyExists(object, BODY_WIDTH_PROP) &&
                    TiledUtils.propertyExists(object,BODY_HEIGHT_PROP)) {
                float bodyWidth = TiledUtils.getFloatProperty(object, BODY_WIDTH_PROP);
                float bodyHeight = TiledUtils.getFloatProperty(object,BODY_HEIGHT_PROP);

                bodySkeleton = new RectangleMapObject();
                ((RectangleMapObject)bodySkeleton).getRectangle().setSize(bodyWidth, bodyHeight);
            }

            int layerPos = getLayerPos(layer, object);
            BodyDef bodyDef = getBodyDef(layer, object);
            EntityDefinition entityDef = new TiledEntityDefinition(object.getName(), type, layerPos,
                    bodyDef, bodySkeleton, object.getProperties());
            entityDefinitions.add(entityDef);
        }
    }

    private void setBackground() {
        // Format: "texture1, 0.8, texture2, 0.3, ..."
        String[] backgroundInfo = tiledMap.getProperties().get("background").toString().split(", ");
        for (int i = 0; i < backgroundInfo.length; i += 2) {
            String textureName = backgroundInfo[i];
            float parallaxRatio = Float.parseFloat(backgroundInfo[i+1]);
            background.addLayer(new TextureParallaxLayer(parallaxRatio, textureName));
        }
    }

    private int getLayerPos(MapLayerWrapper layer, TextureMapObject object) {
        if (TiledUtils.propertyExists(object, LAYER_POS_PROP)) {
            return TiledUtils.getIntProperty(object, LAYER_POS_PROP);
        } else if (TiledUtils.propertyExists(layer, LAYER_POS_PROP)) {
            return TiledUtils.getIntProperty(layer, LAYER_POS_PROP);
        } else {
            throw new InvalidConfigException(filename, LAYER_POS_PROP, "null");
        }
    }

    private BodyDef getBodyDef(MapLayerWrapper layer, MapObject object) {
        BodyType bodyType = TiledUtils.getBodyType(object);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(getObjectPosition(object));

        return bodyDef;
    }

    private Vector2 getObjectPosition( MapObject object) {
        float unitScale = MainCamera.getInstance().getTileMapScale();
        float width = TiledUtils.getFloatProperty(object, WIDTH_PROP) * unitScale;
        float height = TiledUtils.getFloatProperty(object, HEIGHT_PROP) * unitScale;
        float x = (TiledUtils.getFloatProperty(object, X_PROP) * unitScale) + (width / 2);
        float y = (TiledUtils.getFloatProperty(object, Y_PROP) * unitScale) - (height / 2);

        return new Vector2(x, y);
    }

    private boolean isLayerPosValid(int layerPos) {
        return layerPos > -1 && layerPos < Canvas.NUM_LAYERS;
    }

    private boolean isBodySkeleton(String type) {
        return type != null && type.equals(BODY_SKELETON_TYPE);
    }
}
