package com.tendersaucer.collector;

/**
 * Game entry point
 *
 * Created by Alex on 4/8/2016.
 */
public final class Game extends com.badlogic.gdx.Game {

	@Override
	public void create () {
		setScreen(new Driver());
	}
}
