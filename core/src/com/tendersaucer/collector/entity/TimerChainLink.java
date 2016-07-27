package com.tendersaucer.collector.entity;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.GameState;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.event.EventManager;
import com.tendersaucer.collector.event.GameStateChangeEvent;
import com.tendersaucer.collector.event.IGameStateChangeListener;
import com.tendersaucer.collector.level.Level;

/**
 * Created by Alex on 7/23/2016.
 */
public class TimerChainLink extends RenderedEntity implements IGameStateChangeListener {

    private static final String ID_PREFIX = "tcl_";

    private boolean isStart;
    private boolean isEnd;
    private float delay;
    private Long activatedStartTime;

    public TimerChainLink(EntityDefinition def) {
        super(def);

        isStart = def.getBooleanProperty("is_start");
        isEnd = def.getBooleanProperty("is_end");
        delay = def.getFloatProperty("delay");
        activatedStartTime = null;
        body.getFixtureList().get(0).setSensor(true);

        Filter filter = new Filter();
        filter.categoryBits = Player.COLLISION_MASK;
        body.getFixtureList().get(0).setFilterData(filter);
    }

    @Override
    public void init() {
        super.init();

        if (isStart) {
            EventManager.getInstance().listen(GameStateChangeEvent.class, this);
        }
    }

    @Override
    public boolean update() {
        if (activatedStartTime != null && TimeUtils.timeSinceMillis(activatedStartTime) > delay) {
            deactivate();

            if (!isEnd) {
                int currId = Integer.valueOf(getId().replace(ID_PREFIX, ""));
                String nextId = ID_PREFIX + (currId + 1);
                ((TimerChainLink)Level.getInstance().getEntity(nextId)).activate();
            } else if (Globals.getGameState() == GameState.RUNNING){
                Level.getInstance().replay();
            }
        }

        return super.update();
    }

    @Override
    public void onGameStateChange(GameState oldEvent, GameState newEvent) {
        if (newEvent == GameState.RUNNING) {
            activate();
        }
    }

    private void activate() {
        activatedStartTime = TimeUtils.millis();
        sprite.setColor(0, 0, 0, 1);
    }

    private void deactivate() {
        activatedStartTime = null;
        //sprite.setColor(1, 1, 1, 1);
    }
}
