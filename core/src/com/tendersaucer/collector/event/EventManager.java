package com.tendersaucer.collector.event;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages most game events and their listeners in one consolidated place
 * However, some are not handled here (e.g ICollide)
 *
 * Created by Alex on 5/5/2016.
 */
public final class EventManager {

    private static final EventManager instance = new EventManager();

    private final Map<Class<? extends Event>, ArrayList> eventListeners;

    private EventManager() {
        eventListeners = new ConcurrentHashMap<Class<? extends Event>, ArrayList>();
    }

    public static EventManager getInstance() {
        return instance;
    }

    public <L> void listen(Class<? extends Event<L>> eventClass, L listener) {
        ArrayList listeners = eventListeners.get(eventClass);
        if (listeners == null) {
            listeners = new ArrayList();
        }

        listeners.add(listener);

        eventListeners.put(eventClass, listeners);
    }

    public <L> void mute(Class<? extends Event<L>> eventClass, L listener) {
        ArrayList listeners = eventListeners.get(eventClass);
        if (listeners != null) {
            listeners.remove(listener);
            eventListeners.put(eventClass, listeners);
        }
    }

    public <L> void clear(Class<? extends Event<L>> eventClass) {
        eventListeners.remove(eventClass);
    }

    public <L> void notify(Event<L> event) {
        Class<Event<L>> eventClass = (Class<Event<L>>)event.getClass();
        if (eventListeners.containsKey(eventClass)) {
            for (L listener : (ArrayList<L>)eventListeners.get(eventClass)) {
                event.notify(listener);
            }
        }
    }

    public <L> void notify(Class<? extends Event<L>> eventClass) {
        try {
            Event<L> event = eventClass.newInstance();
            if (eventListeners.containsKey(eventClass)) {
                for (L listener : (ArrayList<L>)eventListeners.get(eventClass)) {
                    event.notify(listener);
                }
            }
        } catch(InstantiationException e) {
            // TODO:
        } catch (IllegalAccessException e) {
            // TODO:
        }
    }

    public <L> void postNotify(final Event<L> event) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                EventManager.this.notify(event);
            }
        });
    }

    public <L> void postNotify(final Class<? extends Event<L>> eventClass) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                EventManager.this.notify(eventClass);
            }
        });
    }
}
