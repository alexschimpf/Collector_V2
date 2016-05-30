package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.Color;
import com.tendersaucer.collector.event.ColorSystemNewColorEvent;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.IColorSystemNewColorListener;

/**
 * Created by Alex on 5/26/2016.
 */
public class ColorSystemPlatform extends RenderedEntity implements IColorSystemNewColorListener {

    private final String providerId;
    private Color currentColor;

    public ColorSystemPlatform(EntityDefinition definition) {
        super(definition);

        sprite.setColor(definition.getColorProperty("color"));
        providerId = definition.getStringProperty("provider_id");
        body.setActive(false);
    }

    @Override
    public void init() {
        super.init();

        EventManager.getInstance().listen(ColorSystemNewColorEvent.class, this);
    }

    @Override
    public void onColorSystemNewColor(String providerId, Color newColor) {
        if (this.providerId.equals(providerId)) {
            currentColor = newColor;
        }
    }

    @Override
    protected void tick() {
        super.tick();

        if (!body.isActive() && hasCurrentColor() && overlapsPlayer()) {
            body.setActive(true);
        } else if (body.isActive() && !hasCurrentColor()) {
            body.setActive(false);
        }
    }

    private boolean hasCurrentColor() {
        if (currentColor == null) {
            return false;
        }

        Color color = sprite.getColor();
        return color.r == currentColor.r && color.g == currentColor.g && color.b == currentColor.b;
    }
}
