package com.tendersaucer.collector.statistics;

import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.*;
import com.tendersaucer.collector.event.IGameStateChangeListener;

/**
 * Created by Alex on 7/27/2016.
 */
public final class StatisticsListener implements IGameStateChangeListener {

    private static final String ITERATION_ID_KEY = "iteration_id";
    private static final String LEVEL_ID_KEY = "level_id";
    private static final String RUN_ID_KEY = "run_id";
    private static final String TOTAL_TIME_KEY = "total_time";
    private static final StatisticsListener instance = new StatisticsListener();

    private Long runStartTime;
    private StatisticsDAO dao;

    private StatisticsListener() {
        dao = StatisticsDAO.getInstance();
    }

    public static StatisticsListener getInstance() {
        return instance;
    }

    @Override
    public void onGameStateChange(GameState oldEvent, GameState newEvent) {
        if (isRunBegin(newEvent)) {
            runStartTime = TimeUtils.millis();
        }
        if (isRunEnd(oldEvent, newEvent)) {
            if (runStartTime != null) {
                dao.increment(RUN_ID_KEY);

                long duration = TimeUtils.timeSinceMillis(runStartTime);
                dao.add(TOTAL_TIME_KEY, duration);
                runStartTime = null;
            }
        }
        if (isLevelEnd(newEvent)) {
            dao.reset(RUN_ID_KEY);

            long levelId = dao.getLong(LEVEL_ID_KEY);
            if (levelId >= Globals.NUM_LEVELS - 1) {
                dao.reset(LEVEL_ID_KEY);
                dao.increment(ITERATION_ID_KEY);
            } else {
                dao.increment(LEVEL_ID_KEY);
            }
        }
    }

    public long getIterationId() {
        return dao.getLong(ITERATION_ID_KEY);
    }

    public long getLevelId() {
        return dao.getLong(LEVEL_ID_KEY);
    }

    public long getRunId() {
        return dao.getLong(RUN_ID_KEY);
    }

    public long getTotalTime() {
        return dao.getLong(TOTAL_TIME_KEY);
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
