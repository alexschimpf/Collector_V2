package com.tendersaucer.collector.util;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.tendersaucer.collector.Camera;

/**
 * Tiled utility functions
 *
 * Created by Alex on 4/13/2016.
 */
public final class TiledUtils {

    private static final String BODY_TYPE_PROP = "body_type";
    private static final String KINEMATIC_BODY_TYPE = "kinematic";
    private static final String DYNAMIC_BODY_TYPE = "dynamic";
    private static final String STATIC_BODY_TYPE = "static";

    private TiledUtils() {
    }

    public static FixtureBodyDefinition createRectangleFixtureBodyDef(RectangleMapObject object) {
        BodyDef bodyDef = new BodyDef();
        Rectangle rectangle = object.getRectangle();
        bodyDef.position.x = rectangle.x + (rectangle.width / 2);
        bodyDef.position.y = rectangle.y + (rectangle.height / 2);
        bodyDef.position.scl(Camera.getInstance().getTileMapScale());
        bodyDef.type = getBodyType(object);

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        return new FixtureBodyDefinition(fixtureDef, bodyDef);
    }

    public static FixtureBodyDefinition createCircleFixtureBodyDef(CircleMapObject object) {
        BodyDef bodyDef = new BodyDef();
        Circle circle = object.getCircle();
        bodyDef.position.x = circle.x;
        bodyDef.position.y = circle.y;
        bodyDef.position.scl(Camera.getInstance().getTileMapScale());
        bodyDef.type = getBodyType(object);

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        return new FixtureBodyDefinition(fixtureDef, bodyDef);
    }

    public static FixtureBodyDefinition createEllipseFixtureBodyDef(EllipseMapObject object) {
        BodyDef bodyDef = new BodyDef();
        Ellipse ellipse = object.getEllipse();
        bodyDef.position.x = ellipse.x + (ellipse.width / 2);
        bodyDef.position.y = ellipse.y + (ellipse.height / 2);
        bodyDef.position.scl(Camera.getInstance().getTileMapScale());
        bodyDef.type = getBodyType(object);

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        return new FixtureBodyDefinition(fixtureDef, bodyDef);
    }

    // For polygons and polylines...
    public static FixtureBodyDefinition createPolyFixtureBodyDef(MapObject object) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0); // not sure if this is necessary
        bodyDef.type = getBodyType(object);

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        return new FixtureBodyDefinition(fixtureDef, bodyDef);
    }

    public static FixtureDef getFixtureDefFromBodySkeleton(MapObject object) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;

        Shape shape = null;
        if(object instanceof TextureMapObject) {
            shape = getTextureMapShape(object);
        } else if(object instanceof RectangleMapObject) {
            shape = getRectangleShape(object);
        } else if(object instanceof CircleMapObject) {
            shape = getCircleShape(object);
        } else if(object instanceof EllipseMapObject) {
            shape = getEllipseShape(object);
        } else if(object instanceof PolygonMapObject) {
            shape = getPolygonShape(object);
        } else if(object instanceof PolylineMapObject) {
            shape = getPolylineShape(object);
        }

        fixtureDef.shape = shape;

        return fixtureDef;
    }

    public static BodyType getBodyType(MapObject object) {
        if (!object.getProperties().containsKey(BODY_TYPE_PROP)) {
            return BodyType.StaticBody;
        }

        BodyType bodyType;
        String bodyTypeName = object.getProperties().get(BODY_TYPE_PROP).toString();
        if(bodyTypeName.equals(STATIC_BODY_TYPE)) {
            bodyType = BodyType.StaticBody;
        } else if(bodyTypeName.equals(KINEMATIC_BODY_TYPE)) {
            bodyType = BodyType.KinematicBody;
        } else if(bodyTypeName.equals(DYNAMIC_BODY_TYPE)) {
            bodyType = BodyType.DynamicBody;
        } else {
            throw new InvalidConfigException(BODY_TYPE_PROP, bodyTypeName);
        }

        return bodyType;
    }

    private static Shape getTextureMapShape(MapObject object) {
        TextureMapObject textureMapObject = (TextureMapObject)object;
        float width = (Float)textureMapObject.getProperties().get("width");
        float height = (Float)textureMapObject.getProperties().get("height");

        PolygonShape shape = new PolygonShape();
        float scale = Camera.getInstance().getTileMapScale();
        shape.setAsBox((width / 2) * scale, (height / 2) * scale);

        return shape;
    }

    private static Shape getRectangleShape(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
        PolygonShape shape = new PolygonShape();
        float scale = Camera.getInstance().getTileMapScale();
        shape.setAsBox((rectangle.width / 2) * scale, (rectangle.height / 2) * scale);

        return shape;
    }

    private static Shape getCircleShape(MapObject object) {
        Circle circle = ((CircleMapObject)object).getCircle();
        CircleShape shape = new CircleShape();
        shape.setRadius(circle.radius * Camera.getInstance().getTileMapScale());

        return shape;
    }

    // Just assume the ellipse is a circle for now.
    private static Shape getEllipseShape(MapObject object) {
        Ellipse circle = ((EllipseMapObject)object).getEllipse();
        CircleShape shape = new CircleShape();
        shape.setRadius(circle.width / 2 * Camera.getInstance().getTileMapScale());

        return shape;
    }

    private static Shape getPolygonShape(MapObject object) {
        Polygon polygon = ((PolygonMapObject)object).getPolygon();
        float[] vertices = polygon.getTransformedVertices();
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] *= Camera.getInstance().getTileMapScale();
        }

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        return shape;
    }

    private static Shape getPolylineShape(MapObject object) {
        Polyline polyline = ((PolylineMapObject)object).getPolyline();
        float[] vertices = polyline.getTransformedVertices();
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] *= Camera.getInstance().getTileMapScale();
        }

        ChainShape shape = new ChainShape();
        shape.createChain(vertices);

        return shape;
    }
}
