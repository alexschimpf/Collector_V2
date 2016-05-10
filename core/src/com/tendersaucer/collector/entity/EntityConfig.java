package com.tendersaucer.collector.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.util.InvalidConfigException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alex on 5/9/2016.
 */
public final class EntityConfig {

    private static final EntityConfig instance = new EntityConfig();
    private static final String CONFIG_FILENAME = "entity.json";

    private final Map<String, EntityProperties> entityTypePropertiesMap;
    private final Map<String, String> entityTypeClassMap;

    private EntityConfig() {
        entityTypePropertiesMap = new HashMap<String, EntityProperties>();
        entityTypeClassMap = new HashMap<String, String>();

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
            if (entityType.isEmpty()) {
                throw new InvalidConfigException(CONFIG_FILENAME, "type", "null");
            }

            if (entityTypePropertiesMap.containsKey(entityType)) {
                throw new InvalidConfigException(CONFIG_FILENAME, "type", entityType);
            }

            // Map entity types to their corresponding classes.
            EntityProperties entityProperties = new EntityProperties();
            entityTypePropertiesMap.put(entityType, entityProperties);

            // First add all global properties.
            // Then add all set properties (locally).
            // Local properties override globals.
            JsonValue globalPropertiesRoot = root.get("global_properties");
            addGlobalProperties(globalPropertiesRoot, entityProperties);
            addLocalProperties(entityRoot, entityProperties);
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
            String name = optionalProperty.name;
            String defaultVal = optionalProperty.getString("default");
            entityProperties.addOptionalProperty(name, defaultVal);
        }
    }

    // Add custom properties set locally per entity (which may override global properties).
    private void addLocalProperties(JsonValue entityRoot, EntityProperties entityProperties) {
        String type = entityRoot.getString("type");
        String className = entityRoot.getString("class");
        if (className.isEmpty()) {
            throw new InvalidConfigException(CONFIG_FILENAME, "class", "null");
        }

        entityTypeClassMap.put(type, className);

        // Entity type may not even have custom properties.
        if (!entityRoot.hasChild("properties")) {
            return;
        }

        // Required
        if (entityRoot.get("properties").hasChild("required")){
            for (JsonValue requiredProperty : entityRoot.get("properties").get("required")) {
                String name = requiredProperty.asString();

                // If it was an optional global property, but is now required, remove it as optional.
                if (entityProperties.propertyExists(name) && !entityProperties.isPropertyRequired(name)) {
                    entityProperties.removeOptionalProperty(name);
                }

                entityProperties.addRequiredProperty(name);
            }
        }

        // Optional
        if (entityRoot.get("properties").hasChild("optional")){
            for (JsonValue optionalProperty : entityRoot.get("properties").get("optional")) {
                String name = optionalProperty.asString();

                // If it was a required global property, but is now optional, remove it as required.
                if (entityProperties.propertyExists(name) && !entityProperties.isPropertyRequired(name)) {
                    entityProperties.removeRequiredProperty(name);
                }

                entityProperties.addOptionalProperty(name, optionalProperty.getString("defaultVal"));
            }
        }
    }

    public final class EntityProperties {

        private final Map<String, Boolean> requiredProperties = new HashMap<String, Boolean>();
        private final Map<String, String> propertyDefaultValMap = new HashMap<String, String>();

        public EntityProperties() {
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
