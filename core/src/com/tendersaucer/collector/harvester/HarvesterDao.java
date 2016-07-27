package com.tendersaucer.collector.harvester;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.Map;

/**
 * Super inefficient by easy-to-use DAO for Harvester
 *
 * Created by Alex on 7/27/2016.
 */
public final class HarvesterDao {

    private static final String PREFS_FILE_NAME = "Collector";
    private static final HarvesterDao instance =  new HarvesterDao();

    private Map<String, ?> preferencesMap;
    private Preferences preferences;

    private HarvesterDao() {
        preferences = Gdx.app.getPreferences(PREFS_FILE_NAME);
        loadFromPreferences();
    }

    public static HarvesterDao getInstance() {
        return instance;
    }

    public long getLong(String key) {
        if (!preferencesMap.containsKey(key)) {
            return 0;
        }

        return (Long)preferencesMap.get(key);
    }

    // Always use long over int!
    public void add(String key, long amount) {
        if (preferences.contains(key)) {
            long curr = preferences.getLong(key);
            preferences.putLong(key, curr + amount);
        } else {
            preferences.putLong(key, amount);
        }

        persistToPreferences();
    }

    public void increment(String key) {
        add(key, 1);
    }

    public void reset(String key) {
        if (preferences.contains(key)) {
            preferences.remove(key);
        }

        persistToPreferences();
    }

    private void loadFromPreferences() {
        preferencesMap = preferences.get();
    }

    private void persistToPreferences() {
        preferences.put(preferencesMap);
    }
}
