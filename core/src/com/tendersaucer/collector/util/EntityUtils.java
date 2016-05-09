package com.tendersaucer.collector.util;

import com.tendersaucer.collector.entity.Entity;
import com.tendersaucer.collector.entity.Player;

/**
 * Created by Alex on 5/9/2016.
 */
public final class EntityUtils {

    public static boolean isPlayer(Entity entity) {
        return entity != null && entity.getType().equals(Player.TYPE);
    }
}
