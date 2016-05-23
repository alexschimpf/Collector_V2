package com.tendersaucer.collector.entity;

import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.world.room.Room;

/**
 * Created by Alex on 5/21/2016.
 */
public class DetectionLine extends RenderedEntity {

    protected static final float DEFAULT_ALPHA = 0.2f;

    protected final float duration;
    protected long startTime;

    public DetectionLine(EntityDefinition definition) {
        super(definition);

        duration = definition.getFloatProperty("duration");
        startTime = TimeUtils.millis();

        body.setActive(false);
        sprite.setColor(0, 0, 0, DEFAULT_ALPHA);
    }

    @Override
    public void tick() {
        super.tick();

        if (body.isActive()) {
            return;
        }

        Player player = Room.getInstance().getPlayer();
        if (player.getBottom() >= getTop() || player.getRight() <= getLeft() ||
                player.getLeft() >= getRight()) {
            startTime = TimeUtils.millis();
        } else if (TimeUtils.timeSinceMillis(startTime) > duration) {
            body.setActive(true);
        }

        float percentComplete = Math.min(1, TimeUtils.timeSinceMillis(startTime) / duration);
        sprite.setAlpha(DEFAULT_ALPHA + ((1 - DEFAULT_ALPHA) * percentComplete));
    }
}
