package com.tendersaucer.collector.entity;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tendersaucer.collector.util.BodyDefinition;
import com.tendersaucer.collector.util.FixtureUtils;

/**
 * Created by Alex on 4/12/2016.
 */
public final class EntityDefinition {

    private final String name;
    private final BodyDefinition bodyDef;
    private final FixtureDef fixtureDef;
    private final MapProperties properties;

    public EntityDefinition(String name, BodyDefinition bodyDef, MapProperties properties, MapObject bodyObject) {
        if(name == null || name.isEmpty()) {
            name = getSynthesizedName(bodyDef, properties, bodyObject);
        }

        this.name = name;
        this.bodyDef = bodyDef;
        this.properties = properties;

        fixtureDef = FixtureUtils.getFixtureDef(bodyObject);
    }

    public String getName() {
        return name;
    }

    public Vector2 getPosition() {
        return bodyDef.position;
    }

    public Vector2 getSize() {
        return bodyDef.size;
    }

    public BodyDef.BodyType getBodyType() {
        return bodyDef.bodyType;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }

    public boolean propertyExists(String key) {
        return properties.containsKey(key);
    }

    public boolean isPropertyEmpty(String key) {
        return !properties.containsKey(key) || properties.get(key).toString().isEmpty();
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public String getStringProperty(String key) {
        return getProperty(key).toString();
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getStringProperty(key));
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(getStringProperty(key));
    }

    public float getFloatProperty(String key) {
        return Float.parseFloat(getStringProperty(key));
    }

    public Vector2 getPropertyVector2(MapObject mapObject, String key) {
        if(getStringProperty(key).isEmpty()) {
            return new Vector2();
        }

        float[] vals = getPropertyFloatArray(key, ",");
        return new Vector2(vals[0], vals[1]);

    }

    public boolean[] getPropertyBooleanArray(String key, String delim) {
        String full = getStringProperty(key);

        if(full.isEmpty()) {
            return new boolean[0];
        }

        String[] strArr = full.split(delim);
        boolean[] booleanArr = new boolean[strArr.length];

        int i = 0;
        for(String elem : strArr) {
            booleanArr[i++] = Boolean.parseBoolean(elem);
        }

        return booleanArr;
    }

    public int[] getPropertyIntArray(String key, String delim) {
        String full = getStringProperty(key);

        if(full.isEmpty()) {
            return new int[0];
        }

        String[] strArr = full.split(delim);
        int[] intArr = new int[strArr.length];

        int i = 0;
        for(String elem : strArr) {
            intArr[i++] = Integer.parseInt(elem);
        }

        return intArr;
    }

    public float[] getPropertyFloatArray(String key, String delim) {
        String full = getStringProperty(key);

        if(full.isEmpty()) {
            return new float[0];
        }

        String[] strArr = full.split(delim);
        float[] floatArr = new float[strArr.length];

        int i = 0;
        for(String elem : strArr) {
            floatArr[i++] = Float.parseFloat(elem);
        }

        return floatArr;
    }

    public String[] getPropertyStringArray(String key, String delim) {
        String full = getStringProperty(key);

        if(full.isEmpty()) {
            return new String[0];
        }

        return full.split(delim);
    }

    private String getSynthesizedName(BodyDefinition bodyDef, MapProperties properties, MapObject bodyObject) {
        return String.join(String.valueOf(bodyDef.hashCode()), String.valueOf(properties.hashCode()),
                String.valueOf(bodyObject.hashCode()));
    }
}
