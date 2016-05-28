package com.tendersaucer.collector.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.event.ILevelLoadBeginListener;
import com.tendersaucer.collector.particle.modifiers.ParticleModifier;
import com.tendersaucer.collector.level.ILevelLoadable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Alex on 4/30/2016.xA
 */
public class ParticleEffectManager implements IUpdate, ILevelLoadBeginListener {

    private static final ParticleEffectManager instance = new ParticleEffectManager();
    private static final String CONFIG_FILENAME = "particle.json";
    private static final String MODIFIERS_CLASS_PATH = "com.tendersaucer.collector.particle.modifiers.";

    private final Array<ParticleEffect> effects;
    private final Map<String, Class<? extends ParticleModifier>> modifierClassMap;
    private final Map<String, JsonValue> effectDefinitionMap;

    private ParticleEffectManager() {
        effects = new Array<ParticleEffect>();
        modifierClassMap = new ConcurrentHashMap<String, Class<? extends ParticleModifier>>();
        effectDefinitionMap = new ConcurrentHashMap<String, JsonValue>();
    }

    public static ParticleEffectManager getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        Iterator<ParticleEffect> effectsIter = effects.iterator();
        while (effectsIter.hasNext()) {
            ParticleEffect effect = effectsIter.next();
            if (effect.update()) {
                effect.dispose();
                effectsIter.remove();
            }
        }

        return false;
    }

    @Override
    public void onLevelLoadBegin(ILevelLoadable loadable) {
        clearLiveEffects();
    }

    public void clearLiveEffects() {
        for (ParticleEffect effect : effects) {
            effect.dispose();
        }

        effects.clear();
    }

    public void loadDefinitions() {
        JsonReader jsonReader = new JsonReader();
        JsonValue root = jsonReader.parse(Gdx.files.internal(CONFIG_FILENAME));

        // Load modifier definitions.
        for (JsonValue child : root.get("modifiers")) {
            String className = MODIFIERS_CLASS_PATH + child.asString();
            try {
                modifierClassMap.put(child.name,
                        (Class<? extends ParticleModifier>)Class.forName(className));
            } catch (ClassNotFoundException e) {
                Gdx.app.log("particle", e.toString());
            }
        }

        // Load effect definitions.
        for (JsonValue child : root.get("effects")) {
            effectDefinitionMap.put(child.name, child);
        }
    }

    public Array<String> getParticleEffectTypes() {
        Array<String> effectTypes = new Array<String>();
        for (String effectType : effectDefinitionMap.keySet()) {
            effectTypes.add(effectType);
        }

        return effectTypes;
    }

    public void beginParticleEffect(String type, Vector2 position, Vector2 sizeRange, int layer) {
        ParticleEffect effect = buildParticleEffect(type);
        effects.add(effect);

        effect.begin(position, sizeRange, layer);
    }

    public void beginParticleEffect(ParticleEffect effect, Vector2 position, Vector2 sizeRange,
                                    int layer) {
        effects.add(effect);
        effect.begin(position, sizeRange, layer);
    }

    public ParticleEffect buildParticleEffect(String type) {
        JsonValue definition = effectDefinitionMap.get(type);
        return new ParticleEffect(definition);
    }

    public ParticleModifier buildParticleModifier(JsonValue json) {
        ParticleModifier modifier = null;
        try {
            String type = json.name;
            Class<?> c = modifierClassMap.get(type);
            Constructor<?> constructor = c.getConstructor(JsonValue.class);
            modifier = (ParticleModifier)constructor.newInstance(json);
        } catch (NoSuchMethodException e) {
            Gdx.app.log("particle", e.toString());
        } catch (InstantiationException e) {
            Gdx.app.log("particle", e.toString());
        } catch (IllegalAccessException e) {
            Gdx.app.log("particle", e.toString());
        } catch (InvocationTargetException e) {
            Gdx.app.log("particle", e.toString());
        }

        return modifier;
    }
}