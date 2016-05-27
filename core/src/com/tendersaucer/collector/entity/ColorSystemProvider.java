package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.event.ColorSystemNewColorEvent;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.util.ConversionUtils;
import com.tendersaucer.collector.util.RandomUtils;

/**
 * Created by Alex on 5/26/2016.
 */
public class ColorSystemProvider extends RenderedEntity {

    private final float changeDelay;
    private long lastChangeTime;
    private final Array<Color> colors;
    private int currColorIndex;

    public ColorSystemProvider(EntityDefinition definition) {
        super(definition);

        lastChangeTime = 0;
        changeDelay = definition.getFloatProperty("change_delay");
        colors = getColors();
        currColorIndex = RandomUtils.pickIndex(colors);

        body.setActive(false);
    }

    @Override
    public void tick() {
        super.tick();

        if (TimeUtils.timeSinceMillis(lastChangeTime) > changeDelay) {
            changeColor();
        }
    }

    private void changeColor() {
        lastChangeTime = TimeUtils.millis();

        Color newColor = colors.get(currColorIndex);
        sprite.setColor(newColor);
        EventManager.getInstance().notify(new ColorSystemNewColorEvent(getId(), newColor));

        currColorIndex = (currColorIndex + 1) % colors.size;
    }

    private Array<Color> getColors() {
        Array<Color> colors = new Array<Color>();
        String[] colorStrings = definition.getStringArrayProperty("colors", ";");
        for (String colorString : colorStrings) {
            colors.add(ConversionUtils.toColor(colorString));
        }

        return colors;
    }
}
