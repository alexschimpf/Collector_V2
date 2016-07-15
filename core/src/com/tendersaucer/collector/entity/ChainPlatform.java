package com.tendersaucer.collector.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.animation.AnimatedSprite;
import com.tendersaucer.collector.event.ChainResetEvent;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.IChainResetListener;
import com.tendersaucer.collector.level.Level;

/**
 * Created by Alex on 5/31/2016.
 */
public class ChainPlatform extends RenderedEntity implements IChainResetListener {

    private final String[] nextIds;
    private final String groupId;
    private final boolean isStart;
    private final float activatedDuration;
    private long activatedStartTime;
    private int state;

    public ChainPlatform(EntityDefinition def) {
        super(def);

        nextIds = def.getStringArrayProperty("next_ids", ",");
        groupId = def.getStringProperty("group_id");
        isStart = def.getBooleanProperty("is_start");
        activatedDuration = def.getFloatProperty("activated_duration");

        state = 0;
    }

    @Override
    public void onBeginContact(Contact contact, Entity entity) {
        if (Entity.isPlayer(entity)) {
            if (state >= nextIds.length) {
                // END OF CHAIN
            } else {
                String nextId = nextIds[state++];
                ChainPlatform next = (ChainPlatform)Level.getInstance().getEntity(nextId);
                next.activate();
            }
        }
    }

    @Override
    public void onChainReset(String groupId) {
        if (this.groupId.equals(groupId)) {
            state = 0;
            activatedStartTime = 0;
            ((AnimatedSprite)sprite).stop();
        }
    }

    @Override
    protected void tick() {
        super.tick();

        ((AnimatedSprite)sprite).update();

        if ((isStart && state == 0) || activatedStartTime == 0) {
            return;
        }
        if (TimeUtils.timeSinceMillis(activatedStartTime) > activatedDuration) {
            EventManager.getInstance().notify(new ChainResetEvent(groupId));
        }
    }

    @Override
    protected Sprite createSprite(EntityDefinition definition) {
        float activatedDuration = definition.getFloatProperty("activated_duration");
        AnimatedSprite sprite = new AnimatedSprite("chain", activatedDuration);
        sprite.setSize(getWidth(), getHeight());

        return sprite;
    }

    protected void activate() {
        activatedStartTime = TimeUtils.millis();
        ((AnimatedSprite)sprite).play();
    }
}
