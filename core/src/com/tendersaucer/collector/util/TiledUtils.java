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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.tendersaucer.collector.Camera;
import com.tendersaucer.collector.Globals;

/**
 * Tiled utility functions
 *
 * Created by Alex on 4/13/2016.
 */
public final class TiledUtils {

    private TiledUtils() {
    }

    public static Body createBodyFromRectangle(MapObject object) {
        Rectangle rectangle = ((RectangleMapObject)object).getRectangle();

        float unitScale = Camera.getInstance().getTileMapScale();
        float left = rectangle.x * unitScale;
        float top = rectangle.y * unitScale;
        float width = rectangle.width * unitScale;
        float height = rectangle.height * unitScale;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.x = left + (width / 2);
        bodyDef.position.y = top + (height / 2);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        Body body = Globals.getPhysicsWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();

        return body;
    }

    public static Body createBodyFromPolyline(MapObject object) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        Body body = Globals.getPhysicsWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();

        return body;
    }

    public static Body createBodyFromCircle(MapObject object) {
        Circle circle = ((CircleMapObject)object).getCircle();

        float unitScale = Camera.getInstance().getTileMapScale();
        float x = circle.x * unitScale;
        float y = circle.y * unitScale;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.x = x;
        bodyDef.position.y = y;
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        Body body = Globals.getPhysicsWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();

        return body;
    }

    // Just assume the ellipse is a circle for now.
    public static Body createBodyFromEllipse(MapObject object) {
        Ellipse circle = ((EllipseMapObject)object).getEllipse();

        float unitScale = Camera.getInstance().getTileMapScale();
        float x = (circle.x + circle.width / 2) * unitScale;
        float y = (circle.y + circle.height / 2) * unitScale;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.x = x;
        bodyDef.position.y = y;
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        Body body = Globals.getPhysicsWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();

        return body;
    }

    public static Body createBodyFromPolygon(MapObject object) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = getFixtureDefFromBodySkeleton(object);

        Body body = Globals.getPhysicsWorld().createBody(bodyDef);
        body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();

        return body;
    }

    public static FixtureDef getFixtureDefFromBodySkeleton(MapObject object) {
        return getScaledFixtureDefFromBodySkeleton(object, 1);
    }

    public static FixtureDef getFixtureDefFromBodySkeleton(MapObject object, float scale) {
        return getScaledFixtureDefFromBodySkeleton(object, scale);
    }

    private static FixtureDef getScaledFixtureDefFromBodySkeleton(MapObject object, float scale) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;

        Shape shape = null;
        float unitScale = Camera.getInstance().getTileMapScale();
        if(object instanceof TextureMapObject) {
            shape = getTextureMapShape(object, scale);
        } else if(object instanceof RectangleMapObject) {
            shape = getRectangleShape(object, scale);
        } else if(object instanceof PolylineMapObject) {
            shape = getPolylineShape(object, scale);
        } else if(object instanceof CircleMapObject) {
            shape = getCircleShape(object, scale);
        } else if(object instanceof EllipseMapObject) {
            shape = getEllipseShape(object, scale);
        } else if(object instanceof PolygonMapObject) {
            shape = getPolygonShape(object, scale);
        }

        fixtureDef.shape = shape;

        return fixtureDef;
    }

    private static Shape getTextureMapShape(MapObject object, float scale) {
        float unitScale = Camera.getInstance().getTileMapScale();

        TextureMapObject textureMapObject = (TextureMapObject)object;
        float width = (Float)textureMapObject.getProperties().get("width");
        float height = (Float)textureMapObject.getProperties().get("height");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 * unitScale * scale, height / 2 * unitScale * scale);
        return shape;
    }

    private static Shape getRectangleShape(MapObject object, float scale) {
        float unitScale = Camera.getInstance().getTileMapScale();

        Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rectangle.width / 2 * unitScale * scale, rectangle.height / 2 * unitScale * scale);
        return shape;
    }

    private static Shape getPolylineShape(MapObject object, float scale) {
        Polyline polyline = ((PolylineMapObject)object).getPolyline();
        float[] vertices = polyline.getTransformedVertices();
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] *= Camera.getInstance().getTileMapScale() * scale;
        }

        ChainShape shape = new ChainShape();
        shape.createChain(vertices);

        return shape;
    }

    private static Shape getCircleShape(MapObject object, float scale) {
        Circle circle = ((CircleMapObject)object).getCircle();
        CircleShape shape = new CircleShape();
        shape.setRadius(circle.radius * Camera.getInstance().getTileMapScale() * scale);

        return shape;
    }

    // Just assume the ellipse is a circle for now.
    private static Shape getEllipseShape(MapObject object, float scale) {
        Ellipse circle = ((EllipseMapObject)object).getEllipse();
        CircleShape shape = new CircleShape();
        shape.setRadius(circle.width / 2 * Camera.getInstance().getTileMapScale() * scale);

        return shape;
    }

    private static Shape getPolygonShape(MapObject object, float scale) {
        Polygon polygon = ((PolygonMapObject)object).getPolygon();
        float[] vertices = polygon.getTransformedVertices();
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] *= Camera.getInstance().getTileMapScale() * scale;
        }

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        return shape;
    }
}
