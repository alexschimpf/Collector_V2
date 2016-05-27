package com.tendersaucer.collector.event;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Alex on 5/26/2016.
 */
public interface IColorSystemNewColorListener {

    void onColorSystemNewColor(String providerId, Color color);
}
