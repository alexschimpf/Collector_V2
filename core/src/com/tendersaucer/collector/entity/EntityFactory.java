package com.tendersaucer.collector.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Factory that builds entities from type and definition
 *
 * Created by Alex on 4/14/2016.
 */
public final class EntityFactory {

    private EntityFactory() {
    }

    public static Entity buildEntity(EntityDefinition entityDef) {
        Entity entity = null;
        try {
            String entityType = entityDef.getType();
            String className = EntityConfig.getInstance().getClassName(entityType);
            Class<?> c = Class.forName(className);
            Constructor<?> constructor = c.getConstructor(EntityDefinition.class);
            entity = (Entity)constructor.newInstance(entityDef);
            entity.init(entityDef);
        } catch (ClassNotFoundException e) {
            // TODO:
        } catch (NoSuchMethodException e) {
            // TODO:
        } catch (InstantiationException e) {
            // TODO:
        } catch (IllegalAccessException e) {
            // TODO:
        } catch (InvocationTargetException e) {
            // TODO:
        }

        return entity;
    }
}
