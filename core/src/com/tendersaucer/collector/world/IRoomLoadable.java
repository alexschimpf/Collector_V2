package com.tendersaucer.collector.world;

import com.badlogic.gdx.utils.Array;
import com.tendersaucer.collector.IRender;
import com.tendersaucer.collector.entity.Entity;

import java.util.Map;

/**
 * Interface for a loadable room
 *
 * Created by Alex on 4/9/2016.
 */
public interface IRoomLoadable {

    static final String TYPE_PROP = "type";
    static final String LAYER_POS_PROP = "layer_pos";
    static final String X_PROP = "x";
    static final String Y_PROP = "y";
    static final String WIDTH_PROP = "width";
    static final String HEIGHT_PROP = "height";
    static final String BODY_WIDTH_PROP = "body_width";
    static final String BODY_TYPE_PROP = "body_type";
    static final String KINEMATIC_BODY_TYPE = "kinematic";
    static final String DYNAMIC_BODY_TYPE = "dynamic";
    static final String STATIC_BODY_TYPE = "static";
    static final String BODY_HEIGHT_PROP = "body_height";
    static final String BODY_SKELETON_ID_PROP = "body_skeleton_id";
    static final String BODY_SKELETON_TYPE = "body_skeleton";
    static final String PLAYER_TYPE = "player";
    static final String BODIES_LAYER = "bodies";

    public Array<Entity> getEntities();

    public Map<IRender, Integer> getRenderableLayerMap();
}