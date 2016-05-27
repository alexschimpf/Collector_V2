package com.tendersaucer.collector;

import com.tendersaucer.collector.screen.ParticleEffectViewer;

/**
 * Created by Alex on 5/26/2016.
 */
public class ParticleEffectViewerApp extends com.badlogic.gdx.Game {

    @Override
    public void create() {
        setScreen(new ParticleEffectViewer());
    }
}
