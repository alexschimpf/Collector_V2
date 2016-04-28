package com.tendersaucer.collector.particle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.Canvas;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.world.room.IRoomLoadBeginListener;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages game particle effects and holds definitions for each type
 *
 * Each definition is completed by setting a position and sizeRange.
 *
 * Created by Alex on 4/8/2016.
 */
public final class ParticleEffectManager implements IUpdate, IRoomLoadBeginListener {

    private static final ParticleEffectManager instance = new ParticleEffectManager();

    private final Array<ParticleEffect> effects;
    private final Map<ParticleEffectType, ParticleEffect> effectTypeMap;

    private ParticleEffectManager() {
        effects = new Array<ParticleEffect>();
        effectTypeMap = new ConcurrentHashMap<ParticleEffectType, ParticleEffect>();
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
                effectsIter.remove();
            }
        }

        return false;
    }

    @Override
    public void onRoomLoadBegin() {
        effects.clear();
    }

    public void beginEffect(ParticleEffectType type, Vector2 position,
                            Vector2 sizeRange, int layer) {
        ParticleEffect effect = createEffect(type, position, sizeRange);
        addParticleEffect(effect);

        Canvas.getInstance().addToLayer(layer, effect);
    }

    public ParticleEffect createEffect(ParticleEffectType type, Vector2 position,
                                       Vector2 sizeRange) {
        ParticleEffect effect = new ParticleEffect(effectTypeMap.get(type));
        effect.setPosition(position.x, position.y);
        effect.setSizeRange(sizeRange.x, sizeRange.y);

        return effect;
    }

    public void addParticleEffect(ParticleEffect effect) {
        effects.add(effect);
    }

    public void loadAllEffectTypes() {
        effectTypeMap.clear();
        for (ParticleEffectType type : ParticleEffectType.values()) {
            ParticleEffect effect = ParticleEffectFactory.getParticleEffect(type);
            effectTypeMap.put(type, effect);
        }
    }

    public void unloadAllEffectTypes() {
        effectTypeMap.clear();
    }

    public void loadEffectType(ParticleEffectType type) {
        ParticleEffect effect = ParticleEffectFactory.getParticleEffect(type);
        effectTypeMap.put(type, effect);
    }

    public void unloadEffectType(ParticleEffectType type) {
        effectTypeMap.remove(type);
    }
}
