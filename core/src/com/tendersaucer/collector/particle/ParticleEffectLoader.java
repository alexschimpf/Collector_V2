package com.tendersaucer.collector.particle;

/**
 * Loads particle effects from config
 *
 * Created by Alex on 4/8/2016.
 */
public final class ParticleEffectLoader {

    public static final ParticleEffectLoader instance = new ParticleEffectLoader();

    private ParticleEffectLoader() {
    }

    public static ParticleEffectLoader getInstance() {
        return instance;
    }
}
