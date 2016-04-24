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

    String TYPE_PROP = "type";
    String LAYER_POS_PROP = "layer_pos";
    String X_PROP = "x";
    String Y_PROP = "y";
    String WIDTH_PROP = "width";
    String HEIGHT_PROP = "height";
    String BODY_WIDTH_PROP = "body_width";
    String BODY_TYPE_PROP = "body_type";
    String KINEMATIC_BODY_TYPE = "kinematic";
    String DYNAMIC_BODY_TYPE = "dynamic";
    String STATIC_BODY_TYPE = "static";
    String BODY_HEIGHT_PROP = "body_height";
    String BODY_SKELETON_ID_PROP = "body_skeleton_id";
    String BODY_SKELETON_TYPE = "body_skeleton";
    String PLAYER_TYPE = "player";
    String BODIES_LAYER = "bodies";

    /**
     * Returns the room's id. This id should be unique within a world
     * @return room's id
     */
    String getId();

    /**
     * TODO
     * @return
     */
    Array<Entity> getEntities();

    /**
     * TODO
     * @return
     */
    Map<IRender, Integer> getRenderableLayerMap();
}