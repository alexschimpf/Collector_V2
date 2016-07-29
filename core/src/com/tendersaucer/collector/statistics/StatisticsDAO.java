package com.tendersaucer.collector.statistics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.tendersaucer.collector.Globals;

import java.util.Map;

/**
 * Inefficient but easy-to-use DAO for StatisticsListener
 *
 * Created by Alex on 7/27/2016.
 */
public final class StatisticsDAO {

    private static final String PREFERENCES_NAME = "Collector";
    public static final String ITERATION_ID_KEY = "iteration_id";
    public static final String LEVEL_ID_KEY = "level_id";
    public static final String RUN_ID_KEY = "run_id";
    public static final String TOTAL_TIME_KEY = "total_time";
    private static final StatisticsDAO instance = new StatisticsDAO();

    private Map<String, ?> preferencesCache;
    private Preferences preferences;

    private StatisticsDAO() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        loadFromPreferences();

        if (Globals.CLEAR_PREFERENCES) {
            clear();
        }
    }

    public static StatisticsDAO getInstance() {
        return instance;
    }

    public long getIterationId() {
        return getLong(ITERATION_ID_KEY);
    }

    public long getLevelId() {
        return getLong(LEVEL_ID_KEY);
    }

    public long getRunId() {
        return getLong(RUN_ID_KEY);
    }

    public long getTotalTime() {
        return getLong(TOTAL_TIME_KEY);
    }

    public long getLong(String key) {
        if (!preferencesCache.containsKey(key)) {
            return 0;
        }

        return Long.parseLong(preferencesCache.get(key).toString());
    }

    public void increment(String key) {
        add(key, 1);
    }

    public void add(String key, long amount) {
        Gdx.app.debug("StatisticsDAO", "Adding " + amount + " to " + key);
        if (preferences.contains(key)) {
            long curr = preferences.getLong(key);
            preferences.putLong(key, curr + amount);
        } else {
            preferences.putLong(key, amount);
        }

        preferences.flush();
        loadFromPreferences();
    }

    public void reset(String key) {
        Gdx.app.debug("StatisticsDAO", "Resetting " + key);
        if (preferences.contains(key)) {
            preferences.remove(key);
        }

        preferences.flush();
        loadFromPreferences();
    }

    private void clear() {
        preferences.clear();
        preferences.flush();
        preferencesCache.clear();
    }

    private void loadFromPreferences() {
        preferencesCache = preferences.get();
    }
}
