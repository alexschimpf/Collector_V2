package com.tendersaucer.collector.statistics;

import com.badlogic.gdx.utils.TimeUtils;
import com.tendersaucer.collector.*;
import com.tendersaucer.collector.event.IGameStateChangeListener;

/**
 * Created by Alex on 7/27/2016.
 */
public final class StatisticsListener implements IGameStateChangeListener {

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
        if (isRunEnd(oldEvent, newEvent) && runStartTime != null) {
            if (!isLevelEnd(newEvent)) {
                dao.increment(StatisticsDAO.RUN_ID_KEY);
            }

            long duration = TimeUtils.timeSinceMillis(runStartTime);
            dao.add(StatisticsDAO.TOTAL_TIME_KEY, duration);
            runStartTime = null;
        }
        if (isLevelFirstRun(oldEvent, newEvent)) {
            dao.reset(StatisticsDAO.RUN_ID_KEY);
            dao.reset(StatisticsDAO.TOTAL_TIME_KEY);
            long levelId = dao.getLong(StatisticsDAO.LEVEL_ID_KEY);
            if (levelId >= Globals.NUM_LEVELS - 1) {
                dao.reset(StatisticsDAO.LEVEL_ID_KEY);
                dao.increment(StatisticsDAO.ITERATION_ID_KEY);
            } else {
                dao.increment(StatisticsDAO.LEVEL_ID_KEY);
            }
        }
    }

    private boolean isRunBegin(GameState newEvent) {
        return newEvent == GameState.RUNNING;
    }

    private boolean isRunEnd(GameState oldEvent, GameState newEvent) {
        return (oldEvent == GameState.RUNNING && newEvent == GameState.WAIT_FOR_INPUT) ||
                isLevelEnd(newEvent);
    }

    private boolean isLevelFirstRun(GameState oldEvent, GameState newEvent) {
        return oldEvent == GameState.LEVEL_COMPLETE && newEvent == GameState.WAIT_FOR_INPUT;
    }

    private boolean isLevelEnd(GameState newEvent) {
        return newEvent == GameState.LEVEL_COMPLETE;
    }
}
