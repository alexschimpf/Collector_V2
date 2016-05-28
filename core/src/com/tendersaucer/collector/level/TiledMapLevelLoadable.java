package com.tendersaucer.collector.level;

import com.badlogic.gdx.Gdx;
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
import com.tendersaucer.collector.AssetManager;
import com.tendersaucer.collector.MainCamera;
import com.tendersaucer.collector.background.ParallaxBackground;
import com.tendersaucer.collector.background.TextureParallaxLayer;
import com.tendersaucer.collector.entity.EntityDefinition;
import com.tendersaucer.collector.entity.TiledEntityDefinition;
import com.tendersaucer.collector.entity.TiledEntityPropertyValidator;
import com.tendersaucer.collector.screen.Canvas;
import com.tendersaucer.collector.screen.Driver;
import com.tendersaucer.collector.screen.IRender;
import com.tendersaucer.collector.util.FixtureBodyDefinition;
import com.tendersaucer.collector.util.InvalidConfigException;
import com.tendersaucer.collector.util.MapLayerWrapper;
import com.tendersaucer.collector.util.TiledUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loadable level from TiledMap
 * <p/>
 * Created by Alex on 4/8/2016.
 */
public final class TiledMapLevelLoadable implements ILevelLoadable {

    private final int id;
    private final String filename;
    private final TiledMap tiledMap;
    private final ParallaxBackground background;
    private final Array<FixtureBodyDefinition> freeBodyDefinitions;
    private final Array<EntityDefinition> entityDefinitions;
    private final Map<IRender, Integer> canvasMap;
    private final Map<String, MapObject> bodySkeletonMap;

    public TiledMapLevelLoadable(int levelId) {
        this.id = levelId;

        freeBodyDefinitions = new Array<FixtureBodyDefinition>();
        entityDefinitions = new Array<EntityDefinition>();
        canvasMap = new LinkedHashMap<IRender, Integer>();
        bodySkeletonMap = new HashMap<String, MapObject>();

        filename = AssetManager.getFilePath("levels", levelId + ".tmx");
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.flipY = false;
        tiledMap = new TmxMapLoader().load(filename, params);

        background = new ParallaxBackground();
        setBackground();

        try {
            processLayers();
        } catch (Exception e) {
            Gdx.app.log("tiled", "Error processing layers");
            Gdx.app.log("tiled", e.toString());
        }
    }

    @Override
    public int getId() {
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
        for (MapLayer layer : tiledMap.getLayers()) {
            MapLayerWrapper layerWrapper = new MapLayerWrapper(renderer, layer);

            // Bodies must exist before entity objects.
            if (layerWrapper.getName().equals("bodies")) {
                processLayer(layerWrapper);
            } else {
                if (!TiledUtils.propertyExists(layerWrapper, "layer")) {
                    throw new InvalidConfigException(filename, "layer", "null");
                }

                int layerPos = TiledUtils.getIntProperty(layerWrapper, "layer");
                if (!isLayerPosValid(layerPos)) {
                    throw new InvalidConfigException(filename, "layer", layerPos);
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
                if (!TiledUtils.propertyExists(object, "type")) {
                    freeBodies.add(object);
                } else {
                    String type = TiledUtils.getStringProperty(object, "type");
                    if (isBodySkeleton(type)) {
                        bodySkeletonMap.put(object.getName(), object);
                    } else {
                        throw new InvalidConfigException(filename, "type", type);
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
            if (!TiledUtils.propertyExists(object, "type")) {
                throw new InvalidConfigException(filename, "type", "null");
            }

            // Layer position may be set from the layer itself
            if (!TiledUtils.propertyExists(object, "layer")) {
                object.getProperties().put("layer", TiledUtils.getIntProperty(layer, "layer"));
            }

            String type = TiledUtils.getStringProperty(object, "type");
            TiledEntityPropertyValidator.validateAndProcess(type, object.getProperties());

            // Determine body skeleton.
            MapObject bodySkeleton = object;
            if (TiledUtils.propertyExists(object, "body_skeleton_id")) {
                String bodySkeletonId = TiledUtils.getStringProperty(object, "body_skeleton_id");
                if (!bodySkeletonMap.containsKey(bodySkeletonId)) {
                    throw new InvalidConfigException(filename, "body_skeleton_id", bodySkeletonId);
                }

                bodySkeleton = bodySkeletonMap.get(bodySkeletonId);
            } else if (TiledUtils.propertyExists(object, "body_width") &&
                    TiledUtils.propertyExists(object, "body_height")) {
                float bodyWidth = TiledUtils.getFloatProperty(object, "body_width");
                float bodyHeight = TiledUtils.getFloatProperty(object, "body_height");
                bodySkeleton = object;
                if (bodyWidth != 0 && bodyHeight != 0) {
                    bodySkeleton = new RectangleMapObject();
                    ((RectangleMapObject)bodySkeleton).getRectangle().setSize(bodyWidth, bodyHeight);
                }
            }

            int layerPos = getLayerPos(layer, object);
            BodyDef bodyDef = getBodyDef(layer, object);
            EntityDefinition entityDefinition = new TiledEntityDefinition(object.getName(), type,
                    layerPos, bodyDef, bodySkeleton, object.getProperties(), object.getTextureRegion());
            entityDefinitions.add(entityDefinition);
        }
    }

    private void setBackground() {
        if (!tiledMap.getProperties().containsKey("background")) {
            return;
        }

        // Format: "texture1, 0.8, texture2, 0.3, ..."
        String[] backgroundInfo = tiledMap.getProperties().get("background").toString().split(", ");
        for (int i = 0; i < backgroundInfo.length; i += 2) {
            String textureName = backgroundInfo[i];
            float parallaxRatio = Float.parseFloat(backgroundInfo[i + 1]);
            background.addLayer(new TextureParallaxLayer(parallaxRatio, textureName));
        }
    }

    private int getLayerPos(MapLayerWrapper layer, TextureMapObject object) {
     if (TiledUtils.propertyExists(object, "layer")) {
        return TiledUtils.getIntProperty(object, "layer");
    } else if (TiledUtils.propertyExists(layer, "layer")) {
        return TiledUtils.getIntProperty(layer, "layer");
    } else {
        throw new InvalidConfigException(filename, "layer", "null");
    }
    }

    private BodyDef getBodyDef(MapLayerWrapper layer, MapObject object) {
        BodyType bodyType = TiledUtils.getBodyType(object);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(getObjectPosition(object));

        return bodyDef;
    }

    private Vector2 getObjectPosition(MapObject object) {
        float unitScale = MainCamera.getInstance().getTileMapScale();
        float width = TiledUtils.getFloatProperty(object, "width") * unitScale;
        float height = TiledUtils.getFloatProperty(object, "height") * unitScale;
        float x = (TiledUtils.getFloatProperty(object, "x") * unitScale) + (width / 2);
        float y = (TiledUtils.getFloatProperty(object, "y") * unitScale) - (height / 2);

        return new Vector2(x, y);
    }

    private boolean isLayerPosValid(int layerPos) {
        return layerPos > -1 && layerPos < Canvas.NUM_LAYERS;
    }

    private boolean isBodySkeleton(String type) {
        return type != null && type.equals("body_skeleton");
    }
}
