package com.tendersaucer.collector.event;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Alex on 5/26/2016.
 */
public class ColorSystemNewColorEvent extends Event<IColorSystemNewColorListener> {

    private final String providerId;
    private final Color newColor;

    public ColorSystemNewColorEvent(String providerId, Color newColor) {
        this.providerId = providerId;
        this.newColor = newColor;
    }

    @Override
    public void notify(IColorSystemNewColorListener listener) {
        listener.onColorSystemNewColor(providerId, newColor);
    }
}
