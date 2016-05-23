package com.tendersaucer.collector.entity;

import com.badlogic.gdx.Gdx;

import java.lang.reflect.Constructor;

/**
 * Factory that builds entities from type and definition
 * <p/>
 * Created by Alex on 4/14/2016.
 */
public final class EntityFactory {

    private static final String ENTITIES_CLASS_PATH = "com.tendersaucer.collector.entity.";

    private EntityFactory() {
    }

    public static Entity buildEntity(EntityDefinition entityDef) {
        Entity entity = null;
        try {
            String entityType = entityDef.getType();
            String className = ENTITIES_CLASS_PATH + EntityConfig.getInstance().getClassName(entityType);
            Class<?> c = Class.forName(className);
            Constructor<?> constructor = c.getConstructor(EntityDefinition.class);
            entity = (Entity)constructor.newInstance(entityDef);
            entity.init();
        } catch (Exception e) {
            String entityInfo = "type=" + entityDef.getType() + ", id=" + entityDef.getId();
            Gdx.app.log("entity", "Error building entity (" + entityInfo + ")");
            Gdx.app.log("entity", e.toString());
        }

        return entity;
    }
}
