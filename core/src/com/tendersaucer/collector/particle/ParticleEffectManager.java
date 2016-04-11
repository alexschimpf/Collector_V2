package com.tendersaucer.collector.particle;

/**
 * Manages game particle effects
 *
 * Created by Alex on 4/8/2016.
 */
public final class ParticleEffectManager {

    private static final ParticleEffectManager instance = new ParticleEffectManager();

    private ParticleEffectManager() {
    }

    public static ParticleEffectManager getInstance() {
        return instance;
    }
}
