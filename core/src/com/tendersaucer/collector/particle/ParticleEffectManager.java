package com.tendersaucer.collector.particle;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IUpdate;
import com.tendersaucer.collector.world.room.IRoomLoadBeginListener;

/**
 * Manages game particle effects
 *
 * Created by Alex on 4/8/2016.
 */
public final class ParticleEffectManager implements IUpdate, IRoomLoadBeginListener {

    private static final ParticleEffectManager instance = new ParticleEffectManager();

    private final Array<ParticleEffect> particleEffects;

    private ParticleEffectManager() {
        particleEffects = new Array<ParticleEffect>();
    }

    public static ParticleEffectManager getInstance() {
        return instance;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void onRoomLoadBegin() {
        particleEffects.clear();
    }

    public ParticleEffect buildParticleEffect(String key, float x, float y) {
        return null;
    }

    public ParticleEffect buildAndAddParticleEffect(String key, float x, float y) {
        ParticleEffect particleEffect = buildParticleEffect(key, x, y);
        addLiveParticleEffect(particleEffect);

        return particleEffect;
    }

    public void addLiveParticleEffect(ParticleEffect particleEffect) {
        particleEffects.add(particleEffect);
    }
}
