package com.tendersaucer.collector;

import com.tendersaucer.collector.screen.Driver;
import com.tendersaucer.collector.screen.ParticleEffectViewer;

/**
 * Game entry point
 *
 * Created by Alex on 4/8/2016.
 */
public final class Game extends com.badlogic.gdx.Game {

	@Override
	public void create () {
		if (Globals.SHOW_PARTICLE_EFFECT_VIEWER) {
			setScreen(new ParticleEffectViewer());
		} else {
			setScreen(new Driver());
		}
	}
}
