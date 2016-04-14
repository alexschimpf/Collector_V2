package com.tendersaucer.collector.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.tendersaucer.collector.util.BodyDefinition;

/**
 * Abstract entity definition
 *
 * Created by Alex on 4/13/2016.
 */
public abstract class EntityDefinition {

    protected final String name;
    protected final BodyDefinition bodyDef;

    public EntityDefinition(String name, BodyDefinition bodyDef) {
        this.name = validateName(name);
        this.bodyDef = bodyDef;
    }

    public abstract Object getProperty(String key);

    public abstract FixtureDef getFixtureDef();

    public String getName() {
        return name;
    }

    public BodyDefinition getBodyDef() {
        return bodyDef;
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

    public boolean propertyExists(String key) {
        return getStringProperty(key) != null;
    }

    public boolean isPropertyEmpty(String key) {
        return !propertyExists(key) || getStringProperty(key).isEmpty();
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

    public Vector2 getVector2Property(String key) {
        if(getStringProperty(key).isEmpty()) {
            return new Vector2();
        }

        float[] vals = getFloatArrayProperty(key, ",");
        return new Vector2(vals[0], vals[1]);

    }

    public boolean[] getBooleanArrayProperty(String key, String delim) {
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

    public int[] getIntArrayProperty(String key, String delim) {
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

    public float[] getFloatArrayProperty(String key, String delim) {
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

    public String[] getStringArrayProperty(String key, String delim) {
        String full = getStringProperty(key);

        if(full.isEmpty()) {
            return new String[0];
        }

        return full.split(delim);
    }

    private String validateName(String name) {
        if (name != null && !name.isEmpty()) {
            return name;
        }

        return String.valueOf(MathUtils.random(0, Integer.MAX_VALUE));
    }
}