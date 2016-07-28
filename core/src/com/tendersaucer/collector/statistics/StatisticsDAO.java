package com.tendersaucer.collector.statistics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Map;

/**
 * Inefficient but easy-to-use DAO for StatisticsListener
 *
 * Created by Alex on 7/27/2016.
 */
public final class StatisticsDAO {

    private static final String PREFERENCES_NAME = "Collector";
    private static final StatisticsDAO instance = new StatisticsDAO();

    private Map<String, ?> preferencesMap;
    private Preferences preferences;

    private StatisticsDAO() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
        loadFromPreferences();
    }

    public static StatisticsDAO getInstance() {
        return instance;
    }

    public long getLong(String key) {
        if (!preferencesMap.containsKey(key)) {
            return 0;
        }

        return Long.parseLong(preferencesMap.get(key).toString());
    }

    public void increment(String key) {
        add(key, 1);
    }

    public void add(String key, long amount) {
        if (preferences.contains(key)) {
            long curr = preferences.getLong(key);
            preferences.putLong(key, curr + amount);
        } else {
            preferences.putLong(key, amount);
        }

        preferences.flush();
    }

    public void reset(String key) {
        if (preferences.contains(key)) {
            preferences.remove(key);
        }

        preferences.flush();
    }

    private void loadFromPreferences() {
        preferencesMap = preferences.get();
    }
}
