package com.tendersaucer.collector.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.tendersaucer.collector.util.InvalidConfigException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Gives access to configured entity properties
 *
 * Created by Alex on 4/14/2016.
 */
public final class EntityConfig {

    private static final EntityConfig instance = new EntityConfig();
    private static final String CONFIG_FILENAME = "entity.xml";

    private final Map<String, EntityProperties> entityTypePropertiesMap;
    private final Map<String, String> entityTypeClassMap;

    private EntityConfig() {
        entityTypePropertiesMap = new HashMap<String, EntityProperties>();
        entityTypeClassMap = new HashMap<String, String>();

        try {
            XmlReader reader = new XmlReader();
            XmlReader.Element root = reader.parse(Gdx.files.internal(CONFIG_FILENAME));
            parseConfig(root);
        } catch (IOException e) {
            // TODO:
        }
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

    private void parseConfig(XmlReader.Element root) {
        XmlReader.Element entitiesRoot = root.getChildByName("entities");
        for (XmlReader.Element entityRoot : entitiesRoot.getChildrenByName("entity")) {
            String entityType = entityRoot.getAttribute("type");
            if (entityType.isEmpty()) {
                throw new InvalidConfigException(CONFIG_FILENAME, "type", "null");
            }

            if (entityTypePropertiesMap.containsKey(entityType)) {
                throw new InvalidConfigException(CONFIG_FILENAME, "type", entityType);
            }

            EntityProperties entityProperties = new EntityProperties();
            entityTypePropertiesMap.put(entityType, entityProperties);

            XmlReader.Element globalPropertiesRoot = root.getChildByName("global_properties");
            addGlobalProperties(globalPropertiesRoot, entityProperties);
            addLocalProperties(entityRoot, entityProperties);
        }
    }

    private void addGlobalProperties(XmlReader.Element globalPropertiesRoot, EntityProperties entityProperties) {
        // Add global required properties.
        Array<XmlReader.Element> requiredElements =
                globalPropertiesRoot.getChildByName("required").getChildrenByName("property");
        for (XmlReader.Element element : requiredElements) {
            String name = element.getAttribute("name");
            if (name.isEmpty()) {
                throw new InvalidConfigException(CONFIG_FILENAME, "name", "null");
            }

            entityProperties.addRequiredProperty(name);
        }


        // Add global optional properties.
        Array<XmlReader.Element> optionalElements =
                globalPropertiesRoot.getChildByName("optional").getChildrenByName("property");
        for (XmlReader.Element element : optionalElements) {
            String name = element.getAttribute("name");
            if (name.isEmpty()) {
                throw new InvalidConfigException(CONFIG_FILENAME, "name", "null");
            }

            String defaultVal = element.getAttribute("default");
            entityProperties.addOptionalProperty(name, defaultVal);
        }
    }

    private void addLocalProperties(XmlReader.Element entityRoot, EntityProperties entityProperties) {
        String type = entityRoot.getAttribute("type");
        String className = entityRoot.getAttribute("class");
        if (className.isEmpty()) {
            throw new InvalidConfigException(CONFIG_FILENAME, "class", "null");
        }

        entityTypeClassMap.put(type, className);

        XmlReader.Element requiredRoot = entityRoot.getChildByName("required");
        for (XmlReader.Element property : requiredRoot.getChildrenByName("property")) {
            String name = property.getAttribute("name");

            // If it was an optional global property, but is now required, remove it as optional.
            if (entityProperties.propertyExists(name) && !entityProperties.isPropertyRequired(name)) {
                entityProperties.removeOptionalProperty(name);
            }

            entityProperties.addRequiredProperty(name);
        }

        XmlReader.Element optionalRoot = entityRoot.getChildByName("optional");
        for (XmlReader.Element property : optionalRoot.getChildrenByName("property")) {
            String name = property.getAttribute("name");
            String defaultVal = property.getAttribute("default");

            // If it was a required global property, but is now optional, remove it as required.
            if (entityProperties.propertyExists(name) && entityProperties.isPropertyRequired(name)) {
                entityProperties.removeRequiredProperty(name);
            }

            entityProperties.addOptionalProperty(name, defaultVal);
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
