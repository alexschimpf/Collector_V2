package com.tendersaucer.collector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Map;

/**
 * Slow-write, fast-read DAO for StatisticsListener
 *
 * Created by Alex on 7/27/2016.
 */
public final class DAO {

    private static final String PREFERENCES_NAME = "Collector";
    public static final String IS_NEW_KEY = "is_new";
    public static final String ITERATION_ID_KEY = "iteration_id";
    public static final String LEVEL_ID_KEY = "level_id";
    public static final String COLOR_ORDER_KEY = "color_order";
    public static final String RUN_ID_KEY = "run_id";
    public static final String TOTAL_TIME_KEY = "total_time";
    private static final DAO instance = new DAO();

    private Map<String, ?> preferencesCache;
    private Preferences preferences;

    private DAO() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        loadFromPreferences();

        if (Globals.CLEAR_PREFERENCES) {
            clear();
        }
    }

    public static DAO getInstance() {
        return instance;
    }

    public boolean isNew() {
        return getBoolean(IS_NEW_KEY, true);
    }

    public long getIterationId() {
        return getLong(ITERATION_ID_KEY, 0);
    }

    public long getLevelId() {
        return getLong(LEVEL_ID_KEY,0);
    }

    public long getRunId() {
        return getLong(RUN_ID_KEY, 0);
    }

    public long getTotalTime() {
        return getLong(TOTAL_TIME_KEY, 0);
    }

    public long getLong(String key, long defaultValue) {
        if (!preferencesCache.containsKey(key)) {
            return defaultValue;
        }

        return Long.parseLong(preferencesCache.get(key).toString());
    }

    public int getInteger(String key, int defaultValue) {
        if (!preferencesCache.containsKey(key)) {
            return defaultValue;
        }

        return Integer.parseInt(preferencesCache.get(key).toString());
    }

    public String getString(String key, String defaultValue) {
        if (!preferencesCache.containsKey(key)) {
            return defaultValue;
        }

        return preferencesCache.get(key).toString();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (!preferencesCache.containsKey(key)) {
            return defaultValue;
        }

        return Boolean.parseBoolean(preferencesCache.get(key).toString());
    }

    public void putBoolean(String key, boolean value) {
        preferences.putBoolean(key, value);
        preferences.flush();
        loadFromPreferences();
    }

    public void putLong(String key, long value) {
        preferences.putLong(key, value);
        preferences.flush();
        loadFromPreferences();
    }

    public void putString(String key, String value) {
        preferences.putString(key, value);
        preferences.flush();
        loadFromPreferences();
    }

    public void putInteger(String key, int value) {
        preferences.putInteger(key, value);
        preferences.flush();
        loadFromPreferences();
    }

    public void increment(String key) {
        add(key, 1);
    }

    public void add(String key, long amount) {
        Gdx.app.debug("DAO", "Adding " + amount + " to " + key);
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
        Gdx.app.debug("DAO", "Resetting " + key);
        if (preferences.contains(key)) {
            preferences.remove(key);
        }

        preferences.flush();
        loadFromPreferences();
    }

    public void clear() {
        preferences.clear();
        preferences.flush();
        preferencesCache.clear();
    }

    private void loadFromPreferences() {
        preferencesCache = preferences.get();
    }
}
