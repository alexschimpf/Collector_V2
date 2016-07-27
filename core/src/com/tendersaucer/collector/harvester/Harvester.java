package com.tendersaucer.collector.harvester;

import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.GameState;
import com.tendersaucer.collector.Globals;
import com.tendersaucer.collector.event.IGameStateChangeListener;

/**
 * Created by Alex on 7/27/2016.
 */
public final class Harvester implements IGameStateChangeListener {

    private static final String ITERATION_ID_FIELD = "iteration_id";
    private static final String LEVEL_ID_FIELD = "level_id";
    private static final String NUM_RUNS_FIELD = "num_runs";
    private static final String TOTAL_TIME_FIELD = "total_time";
    private static final Harvester instance = new Harvester();

    private Long runStartTime;
    private HarvesterDao dao;

    private Harvester() {
        dao = HarvesterDao.getInstance();
    }

    public static Harvester getInstance() {
        return instance;
    }

    @Override
    public void onGameStateChange(GameState oldEvent, GameState newEvent) {
        if (isRunBegin(newEvent)) {
            runStartTime = TimeUtils.millis();
        }
        if (isRunEnd(oldEvent, newEvent)) {
            if (runStartTime != null) {
                dao.increment(NUM_RUNS_FIELD);

                long duration = TimeUtils.timeSinceMillis(runStartTime);
                dao.add(TOTAL_TIME_FIELD, duration);
                runStartTime = null;
            }
        }
        if (isLevelEnd(newEvent)) {
            dao.increment(LEVEL_ID_FIELD);

            long levelId = dao.getLong(LEVEL_ID_FIELD);
            if (levelId >= Globals.NUM_LEVELS) {
                dao.reset(LEVEL_ID_FIELD);
                dao.increment(ITERATION_ID_FIELD);
            }
        }
    }

    public long getIterationId() {
        return dao.getLong(ITERATION_ID_FIELD);
    }

    public long getLevelId() {
        return dao.getLong(LEVEL_ID_FIELD);
    }

    public long getNumRuns() {
        return dao.getLong(NUM_RUNS_FIELD);
    }

    public long getTotalTime() {
        return dao.getLong(TOTAL_TIME_FIELD);
    }

    private boolean isRunBegin(GameState newEvent) {
        return newEvent == GameState.RUNNING;
    }

    private boolean isRunEnd(GameState oldEvent, GameState newEvent) {
        return (oldEvent == GameState.RUNNING && newEvent == GameState.WAIT_FOR_INPUT) ||
                isLevelEnd(newEvent);
    }

    private boolean isLevelEnd(GameState newEvent) {
        return newEvent == GameState.LEVEL_COMPLETE;
    }
}
