package com.tendersaucer.collector.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Alex on 4/14/2016.
 */
public class EntityFactory {

    public static Entity buildEntity(String type, EntityDefinition entityDef) {
        Entity entity = null;
        try {
            String className = EntityConfig.getInstance().getClassName(type);
            Class<?> c = Class.forName(className);
            Constructor<?> constructor = c.getConstructor(EntityDefinition.class);
            entity = (Entity)constructor.newInstance(entityDef);
            entity.onCreate(entityDef);
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
