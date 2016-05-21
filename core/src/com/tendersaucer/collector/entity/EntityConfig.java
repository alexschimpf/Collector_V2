package com.tendersaucer.collector.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.util.InvalidConfigException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alex on 5/9/2016.
 */
public final class EntityConfig {

    private static final EntityConfig instance = new EntityConfig();
    private static final String CONFIG_FILENAME = "entity.json";

    private final Map<String, EntityProperties> entityTypePropertiesMap;
    private final Map<String, String> entityTypeClassMap;

    private EntityConfig() {
        entityTypePropertiesMap = new ConcurrentHashMap<String, EntityProperties>();
        entityTypeClassMap = new ConcurrentHashMap<String, String>();

        JsonReader jsonReader = new JsonReader();
        parseConfig(jsonReader.parse(Gdx.files.internal(CONFIG_FILENAME)));
    }

    public static EntityConfig getInstance() {
        return instance;
    }

    public String getClassName(String entityType) {
        return entityTypeClassMap.get(entityType);
    }

    public EntityProperties getEntityProperties(String entityType) {
        return entityTypePropertiesMap.get(entityType);
    }

    private void parseConfig(JsonValue root) {
        for (JsonValue entityRoot : root.get("entities")) {
            String entityType = entityRoot.name;
            if (entityType.equals("")) {
                throw new InvalidConfigException(CONFIG_FILENAME, "type", "null");
            }

            EntityProperties entityProperties = new EntityProperties();
            addGlobalProperties(root.get("global_properties"), entityProperties);
            addCustomProperties(entityRoot, entityProperties);
            entityTypePropertiesMap.put(entityType, entityProperties);
        }
    }

    // Add a set of defined default global properties (and default values).
    private void addGlobalProperties(JsonValue globalPropertiesRoot,
                                     EntityProperties entityProperties) {
        // Required
        for (JsonValue requiredName : globalPropertiesRoot.get("required")) {
            entityProperties.addRequiredProperty(requiredName.asString());
        }

        // Optional
        for (JsonValue optionalProperty : globalPropertiesRoot.get("optional")) {
            String name = optionalProperty.getString("name");
            String defaultVal = optionalProperty.getString("default");
            entityProperties.addOptionalProperty(name, defaultVal);
        }
    }

    // Add custom properties set per entity (which may override global properties).
    private void addCustomProperties(JsonValue entityRoot, EntityProperties entityProperties) {
        String type = entityRoot.name;
        String className = entityRoot.getString("class");
        if (className.equals("")) {
            throw new InvalidConfigException(CONFIG_FILENAME, "class", "null");
        }

        entityTypeClassMap.put(type, className);

        // Entity type may not even have custom properties.
        if (!entityRoot.hasChild("properties")) {
            return;
        }

        // Required
        if (entityRoot.get("properties").hasChild("required")) {
            for (JsonValue requiredProperty : entityRoot.get("properties").get("required")) {
                // If it was an optional global property but is now required, remove it as optional.
                String name = requiredProperty.asString();
                if (entityProperties.propertyExists(name) && !entityProperties.isPropertyRequired(name)) {
                    entityProperties.removeOptionalProperty(name);
                }

                entityProperties.addRequiredProperty(name);
            }
        }

        // Optional
        if (entityRoot.get("properties").hasChild("optional")) {
            for (JsonValue optionalProperty : entityRoot.get("properties").get("optional")) {
                // If it was a required global property but is now optional, remove it as required.
                String name = optionalProperty.getString("name");
                if (entityProperties.propertyExists(name) && entityProperties.isPropertyRequired(name)) {
                    entityProperties.removeRequiredProperty(name);
                }

                entityProperties.addOptionalProperty(name, optionalProperty.getString("default"));
            }
        }
    }

    public final class EntityProperties {

        private final Map<String, Boolean> requiredProperties;
        private final Map<String, String> propertyDefaultValMap;

        public EntityProperties() {
            requiredProperties = new HashMap<String, Boolean>();
            propertyDefaultValMap = new HashMap<String, String>();
        }

        public boolean propertyExists(String name) {
            return requiredProperties.containsKey(name) || propertyDefaultValMap.containsKey(name);
        }

        public boolean isPropertyRequired(String name) {
            if (requiredProperties.containsKey(name)) {
                return true;
            } else if (propertyDefaultValMap.containsKey(name)) {
                return false;
            } else {
                throw new InvalidConfigException(CONFIG_FILENAME, "name", name);
            }
        }

        public Set<String> getRequiredProperties() {
            return requiredProperties.keySet();
        }

        public Set<String> getOptionalProperties() {
            return propertyDefaultValMap.keySet();
        }

        public void addRequiredProperty(String name) {
            requiredProperties.put(name, true);
        }

        public void addOptionalProperty(String name, String defaultVal) {
            propertyDefaultValMap.put(name, defaultVal);
        }

        public void removeRequiredProperty(String name) {
            requiredProperties.remove(name);
        }

        public void removeOptionalProperty(String name) {
            propertyDefaultValMap.remove(name);
        }

        public String getPropertyDefaultVal(String name) {
            return propertyDefaultValMap.get(name);
        }
    }
}
