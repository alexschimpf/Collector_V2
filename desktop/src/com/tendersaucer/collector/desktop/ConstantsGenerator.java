package com.tendersaucer.collector.desktop;

import com.badlogic.gdx.Gdx;
import com.tendersaucer.collector.AssetManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex on 5/31/2016.
 */
public class ConstantsGenerator {

    private static final String DESTINATION_DIR = "/Users/Alex/Desktop/libgdx/Collector/core/src/com/tendersaucer/collector/gen";
    private static final String ASSETS_DIR = "/Users/Alex/Desktop/libgdx/Collector/android/assets";
    private static final String ENTITY_FILE = "entity.json";
    private static final String PARTICLE_FILE = "particle.json";
    private static final String ENTITY_CONSTANTS_CLASS = "EntityConstants";
    private static final String PARTICLE_CONSTANTS_CLASS = "ParticleConstants";
    private static final String CONSTANT_PREFIX = "public static final String ";
    private static final String GEN_PACKAGE = "package com.tendersaucer.collector.gen;";

    public static void main(String[] args) {
        generateEntityConstants();
        generateParticleConstants();
    }

    private static void generateEntityConstants() {
        try {
            JSONParser parser = new JSONParser();
            String inputPath = AssetManager.getFilePath(ASSETS_DIR, ENTITY_FILE);
            JSONObject root = (JSONObject)parser.parse(new FileReader(inputPath));

            LinkedList<String> body = new LinkedList<String>();
            root = (JSONObject)root.get("entities");
            generateEntityConstants((JSONObject)root.get("entity"), "entity", body);
            Path outputPath = Paths.get(DESTINATION_DIR, ENTITY_CONSTANTS_CLASS + ".java");
            writeFile(outputPath, ENTITY_CONSTANTS_CLASS, body);
        } catch (Exception e) {
            Gdx.app.log("constants_generator", e.toString());
        }
    }

    private static void generateEntityConstants(JSONObject root, String name,
                                                LinkedList<String> constants) {
        constants.add("\t" + CONSTANT_PREFIX + name.toUpperCase() + " = \"" + name + "\";");
        if (root.containsKey("children")) {
            root = (JSONObject)root.get("children");
            for (Object child : root.keySet()) {
                JSONObject childRoot = (JSONObject)root.get(child);
                generateEntityConstants(childRoot, child.toString(), constants);
            }
        }
    }

    private static void generateParticleConstants() {
        try {
            JSONParser parser = new JSONParser();
            String inputPath = AssetManager.getFilePath(ASSETS_DIR, PARTICLE_FILE);
            JSONObject root = (JSONObject)parser.parse(new FileReader(inputPath));
            root = (JSONObject)root.get("effects");

            LinkedList<String> body = new LinkedList<String>();
            for (Object child : root.keySet()) {
                String name = child.toString();
                body.add("\t" + CONSTANT_PREFIX + name.toUpperCase() + " = \"" + name + "\";");
            }

            Path outputPath = Paths.get(DESTINATION_DIR, PARTICLE_CONSTANTS_CLASS + ".java");
            writeFile(outputPath, PARTICLE_CONSTANTS_CLASS, body);
        } catch (Exception e) {
            Gdx.app.log("constants_generator", e.toString());
        }
    }

    private static void writeFile(Path filePath, String className, LinkedList<String> body)
            throws java.io.IOException {
        List<String> full = new LinkedList<String>();
        full.add(GEN_PACKAGE);
        full.add("");
        full.add("public class " + className + " {");
        full.add("");
        full.addAll(body);
        full.add("}");
        Files.write(filePath, full, Charset.forName("UTF-8"));
    }
}